var searchStr = location.search;
//由于searchStr属性值包括“?”，所以除去该字符
searchStr = searchStr.substr(1);
//将searchStr字符串分割成数组，数组中的每一个元素为一个参数和参数值
var searchs = searchStr.split("&");
//获得第一个参数和值
var address = searchs[0].split("=");
//获取到登陆用户的id
var id = address[1];
var infourl = "http://localhost:8080/AllNews?newsId=" + id;
console.log(infourl);
var tagsurl = "http://localhost:8080/AllTags?newsId=" + id;

function formatDate(unformdate) {
    var temp = new Date(unformdate);
    temp.setMonth(temp.getMonth()+1);
    return temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();
}     


$(document).ready(function() {
    $.ajax({
        type: 'get',
        url: infourl,
        timeout: 10000, //超时时间，毫秒
        success: function(XHR, TS) {
            if (XHR != null || XHR != "") {
                $("#newsId").text(XHR["newsId"]);
                $("#newsTitle").text(XHR["newsTitle"]);
                $("#newsContent").html(XHR["newsContent"]);
                $("#newsDate").text(formatDate(XHR["newsDate"]));
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

