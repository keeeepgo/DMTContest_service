var searchStr = location.search;
//由于searchStr属性值包括“?”，所以除去该字符
searchStr = searchStr.substr(1);
//将searchStr字符串分割成数组，数组中的每一个元素为一个参数和参数值
var searchs = searchStr.split("&");
//获得第一个参数和值
var address = searchs[0].split("=");
//获取到登陆用户的id
var id = address[1];
var infourl = "http://192.168.0.122:8080/api/door/" + id;
var recordurl = 'http://192.168.0.122:8080/api/openRecord';
var spoturl = 'http://192.168.0.122:8080/images/tmp/';
var table_cols = [
    [ //标题栏
        {
            field: 'doorId',
            title: '门',
            width: 150,
            sort: true
        }, {
            field: 'id',
            title: '人员ID',
            width: 100,
            sort: true
        }, {
            field: 'username',
            title: '姓名',
            width: 100,
            sort: true
        }, {
            field: 'showtime',
            title: '时间',
            width: 200,
            sort: true
        }, {
            field: 'status',
            title: '是否通过',
            width: 100,
            sort: true
        }, {
            field: 'note',
            title: '情况',
            sort: true
        }, {
            fixed: 'right',
            width: 200,
            align: 'center',
            toolbar: '#bar'
        }
    ]
];

function formatDate(res) {
    for (var i = 0; i < res.data.length; ++i) {
        res.data[i]["realtime"] = res.data[i]["time"];
        res.data[i]["showtime"] = new Date(res.data[i]["time"]).toLocaleString();
        res.data[i]["time"] = new Date(res.data[i]["time"]);
//        console.log(res.data[i]["time"]);
    }
    return res;
}

function freshtable() {
    layui.use('table', function() {
        var table = layui.table;
        table.render({
            elem: '#people-table',
            width: 1000,
            cols: table_cols,
            done: function(res) {
                res = formatDate(res);
                table.render({
                    elem: '#people-table',
                    data: res.data,
                    width: 1000,
                    cols: table_cols,
                    skin: 'row' //表格风格
                        ,
                    even: true,
                    page: true
                });
            },
            url: recordurl,
            skin: 'row' //表格风格
                ,
            even: true,
            page: true
        });
    });
}

$(document).ready(function() {
    $.ajax({
        type: 'get',
        url: infourl,
        cache: false,
        processData: false,
        timeout: 10000, //超时时间，毫秒
        success: function(XHR, TS) {
            $("#id").text(XHR["id"]);
            $("#responsibleMan").text(XHR["responsibleMan"]);
            $("#tel").text(XHR["tel"]);
        },
        error: function() {
            $("#people-table").text("加载失败");
        }
    });
    layui.use('table', function() {
        var table = layui.table;
        //展示已知数据
        table.render({
            elem: '#people-table',
            width: 1000,
            cols: table_cols,
            done: function(res) {
                res = formatDate(res);
                table.render({
                    elem: '#people-table',
                    data: res.data,
                    width: 1000,
                    cols: table_cols,
                    skin: 'row' //表格风格
                        ,
                    even: true,
                    page: true
                });
            },
            url: recordurl,
            skin: 'row' //表格风格
                ,
            even: true,
            page: true
        });
        table.on('tool(demo)', function(obj) {
            var data = obj.data;
            if (obj.event === 'detail') {
                layer.open({
                    type: 2,
                    title: '人员详情',
                    maxmin: true,
                    skin: 'layui-layer-molv',
                    area: ['500px', '600px'],
                    content: 'peopledetail.html?id=' + obj.data.id,
                });
            }else if(obj.event === 'spot'){
                var imgtime = obj.data.realtime;

                layer.open({
                    type: 2,
                    title: '现场图片',
                    maxmin: true,
                    skin: 'layui-layer-molv',
                    area: ['500px', '500px'],
                    content: spoturl + imgtime + '.jpg',
                });
            }
        });
        $("#fresh").click(function() {
            freshtable();
        });
        $("#search").click(function() {
            var want = $("#searchinput").val();
            layui.use('table', function() {
                var table = layui.table;
                table.render({
                    elem: '#people-table',
                    width: 1000,
                    url: recordurl,
                    cols: table_cols,
                    done: function(res, curr, count) {
                        var want = $("#searchinput").val();
                        var have = 0;
                        for (var i = 0; i < res.data.length; i++) {
                            have = 0;
                            delete res.data[i]["LAY_TABLE_INDEX"];
                            for (var key in res.data[i]) {
                                var temp = res.data[i][key];
                                if(temp == null){
                                    continue;
                                }
                                if(typeof(temp) != "string"){
                                    temp = temp.toString();
                                }
                                if (temp.indexOf(want) != -1) {
                                    have = 1;
                                }
                            }
                            if (have == 0) {
                                res.data.splice(i, 1);
                                i--;
                            }
                        }
                        res = formatDate(res);
                        table.render({
                            elem: '#people-table',
                            data: res.data,
                            width: 1000,
                            cols: table_cols,
                            skin: 'row' //表格风格
                                ,
                            even: true,
                            page: true
                        });
                    }
                });
            });
        });
        $("#searchdate").click(function() {
            layui.use('table', function() {
                var table = layui.table;
                table.render({
                    elem: '#people-table',
                    width: 1000,
                    url: recordurl,
                    cols: table_cols,
                    done: function(res, curr, count) {
                        var want = $("#searchdateinput").val();
                        var have = 0;
                        for (var i = 0; i < res.data.length; i++) {
                            have = 0;
                            delete res.data[i]["LAY_TABLE_INDEX"];
                            for (var key in res.data[i]) {
                                var temp = res.data[i][key];
                                if(temp == null){
                                    continue;
                                }
                                if(typeof(temp) != "string"){
                                    temp = temp.toString();
                                }
                                if (temp.indexOf(want) != -1) {
                                    have = 1;
                                }
                            }
                            if (have == 0) {
                                res.data.splice(i, 1);
                                i--;
                            }
                        }
                        res = formatDate(res);
                        table.render({
                            elem: '#people-table',
                            data: res.data,
                            width: 1000,
                            cols: table_cols,
                            skin: 'row' //表格风格
                                ,
                            even: true,
                            page: true
                        });
                    }
                });
            });
        });
    });
});

