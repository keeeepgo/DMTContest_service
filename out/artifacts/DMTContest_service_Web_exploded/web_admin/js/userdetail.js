var searchStr = location.search;
//由于searchStr属性值包括“?”，所以除去该字符
searchStr = searchStr.substr(1);
//将searchStr字符串分割成数组，数组中的每一个元素为一个参数和参数值
var searchs = searchStr.split("&");
//获得第一个参数和值
var address = searchs[0].split("=");
//获取到登陆用户的id
var id = address[1];
var infourl = "http://localhost:8080/AllUsers?userId=" + id;
console.log(infourl);
var tagsurl = "http://localhost:8080/AllTags?userId=" + id;

function formatDate(unformdate) {
    var temp = new Date(unformdate);
    temp.setMonth(temp.getMonth()+1);
    return temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();
}     

function formatDate_table(res) {
    for (var i = 0; i < res.data.length; ++i) {
        res.data[i]["realtagDate"] = res.data[i]["tagDate"];
        res.data[i]["showtagDate"] = new Date(res.data[i]["tagDate"]).toLocaleString();
        var temp = new Date(res.data[i]["tagDate"]);
        temp.setMonth(temp.getMonth()+1);
        res.data[i]["tagDate"] = temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();
   }
    return res;
} 

$(document).ready(function() {
    $.ajax({
        type: 'get',
        url: infourl,
        timeout: 10000, //超时时间，毫秒
        success: function(XHR, TS) {
            if (XHR != null || XHR != "") {
                $("#userId").text(XHR["userId"]);
                $("#userName").text(XHR["userName"]);
                $("#registerDate").text(formatDate(XHR["registerDate"]));
                $("#loginDate").text(formatDate(XHR["loginDate"]));
            } else {
                $("#tags-table").text("加载失败");
            }
        },
        error: function() {
            $("#tags-table").text("加载失败");
        }
    });
    layui.use(['table', 'element', 'layer'], function() {
        var table = layui.table,
            layer = layui.layer;
        var table_cols = [
            [{
                field: 'tagId',
                title: 'ID',
                width: '20%',
                sort: true
            }, {
                field: 'tagContent',
                title: '标签内容',
                width: '40%',
                sort: true
            }, {
                field: 'tagDate',
                title: '创建日期',
                width: '30%'
            }, {
                fixed: 'right',
                width: 100,
                align: 'center',
                toolbar: '#bar'
            }]
        ];
        
        //展示已知数据
        table.render({
            elem: '#tags-table',
            width: 600,
            cols: [
                [{
                    field: 'tagId',
                    title: 'ID',
                    width: '20%',
                    sort: true
                }, {
                    field: 'tagContent',
                    title: '标签内容',
                    width: '40%',
                    sort: true
                }, {
                    field: 'tagDate',
                    title: '创建日期',
                    width: '30%'
                }, {
                    fixed: 'right',
                    width: 100,
                    align: 'center',
                    toolbar: '#bar'
                }]
            ],
            url: tagsurl,
            done: function(res, curr, count) {
                res = formatDate_table(res);
                table.render({
                    elem: '#tags-table',
                    data: res.data,
                    width: 600,
                    cols: table_cols,
                    skin: 'row' //表格风格
                        ,
                    even: true,
                    page: true
                });
            },
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
                    title: '标签详情',
                    maxmin: true,
                    skin: 'layui-layer-molv',
                    area: ['500px', '600px'],
                    content: 'tagdetail.html?tagId=' + obj.data.tagId,
                });
            }
        });
    });
});

