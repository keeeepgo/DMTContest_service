/*3D标签云*/
var tags = [
{ tagId: 0, tagContent: "暂无标签" }
];



function reloadTagCloud(tags) {
    var tagcloudHTML = "<div class='tagcloud' id='tagcloud'>";
    for (var i = 0; i < tags.length; i++) {
        tagcloudHTML += ("<a>" + tags[i].tagContent + "</a>");
    }
    tagcloudHTML += "</div>";
    document.getElementById("wrapper").innerHTML = tagcloudHTML;
    tagcloud({
        selector: ".tagcloud", //元素选择器
        fontsize: 16, //基本字体大小, 单位px
        radius: 100, //滚动半径, 单位px
        mspeed: "normal", //滚动最大速度, 取值: slow, normal(默认), fast
        ispeed: "normal", //滚动初速度, 取值: slow, normal(默认), fast
        direction: 135, //初始滚动方向, 取值角度(顺时针360): 0对应top, 90对应left, 135对应right-bottom(默认)...
        keep: false //鼠标移出组件后是否继续随鼠标滚动, 取值: false, true(默认) 对应 减速至初速度滚动, 随鼠标滚动
    });
}

reloadTagCloud(tags);

var url_UserTagList = "http://localhost:8080/UserTagList";

var taglist = new Vue({
    el: '#taglist',
    data: {
        newtag: '',
        tags: tags,
    },
    methods: {
        addItem: function() {
            var tagContent = this.newtag;
            var this_list = this;
            $.ajax({
                type: "POST",
                url: url_UserTagList,
                data: "&userId="+userId+"&tagContent="+tagContent,
                success: function(){
                    this_list.newtag = '';  
                    this_list.refreshList();                  
                }
            });

        },
        deleteItemFromList: function(item) {
            // let index = this.tags.indexOf(item)
            // this.tags.splice(index, 1);
            var this_list = this;
            $.ajax({
                url: url_UserTagList+"?userId="+userId+"&tagId="+item.tagId,
                type: "DELETE",
                success: function(){
                    this_list.refreshList();                  
                },
                complete:function(xhr){
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 304)) {
                            this_list.refreshList();
                        }
                    };
                },
                error:function(xhr,str){
                    console.log(xhr);
                    console.log(str);
                }
            });
        },
        refreshList: function(){
            var xhr = new XMLHttpRequest();
            var url = url_UserTagList+"?userId="+userId;
            xhr.open("GET", url, true);
            var this_list = this;
            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 304)) {
                    var str = "";
                    str = xhr.responseText;
                    newslistdata = JSON.parse(str);
                    tags = newslistdata;
                    this_list.tags = tags;
                    reloadTagCloud(tags);
                }
            };
            xhr.send();
        }
    }
});

taglist.refreshList();