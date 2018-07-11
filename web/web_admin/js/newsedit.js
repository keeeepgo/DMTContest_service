var searchStr = location.search;
//由于searchStr属性值包括“?”，所以除去该字符
searchStr = searchStr.substr(1);
//将searchStr字符串分割成数组，数组中的每一个元素为一个参数和参数值
var searchs = searchStr.split("&");
//获得第一个参数和值
var address = searchs[0].split("=");
//获取到id
var id = address[1];

var infourl = 'http://localhost:8080/AllNews?newsId='+id;

function formatDate(unformdate) {
    var temp = new Date(unformdate);
    temp.setMonth(temp.getMonth()+1);
    return temp.getFullYear()+"-"+temp.getMonth()+"-"+temp.getDate();
}  

$(document).ready(function() {
    $("[name='newsId']").val(id);
    $.ajax({
        type: 'get',
        url: infourl,
        cache: false,
        // dataType: "jsonp", //跨域采用jsonp方式 
        processData: false,
        timeout: 10000, //超时时间，毫秒
        success: function(XHR, TS) {
            if (XHR != null && XHR != "") {
                $("[name='newsDate']").val(formatDate(XHR["newsDate"]));
                $("[name='newsTitle']").val(XHR["newsTitle"]);
                $("[name='newsUrl']").val(XHR["newsUrl"]);
                $("[name='newsContent']").val(XHR["newsContent"]);
            } 
        },
        error: function(XHR, TS) {
            layer.alert('获取信息失败');
        }
    });
    layui.use(['laydate'], function() {
        var laydate = layui.laydate;
            
        laydate.render({
            elem: '#newsDate' //指定元素
        });
    });
});