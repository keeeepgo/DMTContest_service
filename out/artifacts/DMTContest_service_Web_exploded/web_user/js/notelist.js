var nowread_newsId = 1;

Vue.component('togglebutton', {
    props: ['label', 'name'],
    template: `<div class="togglebutton-wrapper" v-bind:class="isactive ? 'togglebutton-checked' : ''">
                  <label v-bind:for="name">
                    <span class="togglebutton-label">{{ label }}</span>
                    <span class="tooglebutton-box"></span>
                  </label>
                  <input v-bind:id="name" type="checkbox" v-bind:name="name" v-model="isactive" v-on:change="onToogle">
              </div>`,
    model: {
        prop: 'checked',
        event: 'change'
    },
    data: function () {
        return {
            isactive: false
        }
    },
    methods: {
        onToogle: function () {
            this.$emit('clicked', this.isactive)
        }
    }
});

function toggleThisDate(thisDate) {
    var all_notelist_li = $.extend(true, {}, $("#notelist_ul").children("li"));
    var temp = $(thisDate).text();
    all_notelist_li.each(function () {
        if ($(this).children(".noteDate_span").text() == temp) {
            $(this).children("span").not(".notelist_DateTitle").slideToggle("slow");;
        }
    });
}

function appendDateLabel() {
    var all_noteDate_span = $("#notelist_ul").find(".noteDate_span");
    var all_noteDate_span = $.extend(true, {}, $("#notelist_ul").find(".noteDate_span"));
    var temp = all_noteDate_span["0"]["innerText"];
    $(all_noteDate_span[0]).prev().before("<i class='material-icons' style='font-size: 3rem;color: #353441;width:10%;'>expand_more</i><span class='notelist_DateTitle' style='width:90%;cursor:pointer'>" + temp + "</span>");
    all_noteDate_span.each(function () {
        if ($(this).text() != temp) {
            temp = $(this).text();
            $(this).prev().before("<i class='material-icons' style='font-size: 3rem;color: #353441;width:10%;'>expand_more</i><span class='notelist_DateTitle' style='width:90%;cursor:pointer'>" + temp + "</span>");
        }
    });

    $(".notelist_DateTitle").click(function () {
        toggleThisDate(this);
    });
}

var url_NoteList = "http://localhost:8080/NoteList?userId=" + userId;
var notelist = new Vue({
    el: '#notelist',
    data: {
        sortByStatus: false,
        sortByDate: false,
        dateTitleShow: false,
        todo: [],
        nowread_noteId: ""

    },
    methods: {
        markAsDoneOrUndone: function (item) {
            item.done = !item.done;

        },
        deleteItemFromList: function (item) {
            let index = this.todo.indexOf(item)
            this.todo.splice(index, 1);
        },
        changeReadNow: function (item) {
            $("#note_content").html(marked(item.noteContent));
        },
        movedonetoogle: function (active) {
            this.sortByStatus = active;
            if (this.dateTitleShow == true && active == true) {
                console.log(this.dateTitleShow);
                $("[name='dateTitleShow']").click();
            }
        },
        moveDatetoogle: function (active) {
            this.sortByDate = active;
        },
        dateTitleShowtoogle: function (active) {
            this.dateTitleShow = active;
            if (active) {
                appendDateLabel();
            } else {
                $(".notelist_DateTitle").prev("i").remove();
                $(".notelist_DateTitle").remove();
            }
        },
        refreshList: function () {
            var xhr = new XMLHttpRequest();
            var url = url_NoteList;
            xhr.open("GET", url, true);
            this_list = this;
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 304)) {
                    var str = "";
                    str = xhr.responseText;
                    var listdata = JSON.parse(str);
                    listdata.forEach(function (element, index, array) {
                        // element: 指向当前元素的值
                        // index: 指向当前索引
                        // array: 指向Array对象本身
                        //console.log(element + ', index = ' + index);
                        element.done = false;
                        var noteDate = new Date(element.noteDate);
                        element.noteDate = noteDate.getFullYear() + '-' + (noteDate.getMonth() + 1) + '-' + noteDate.getDate();;

                    });

                    this_list.todo = listdata;
                }
            };
            xhr.send();
        }
    },
    computed: {
        todoByStatus: function () {
            var sortedArray = [];

            if (this.sortByStatus) {
                var doneArray = this.todo.filter(function (item) {
                    return item.done;
                });
                var notDoneArray = this.todo.filter(function (item) {
                    return !item.done;
                });
                sortedArray = [...notDoneArray, ...doneArray]; //将一个数组转为用逗号分隔的参数序列,取代concat()
            }
            if (this.sortByDate) {
                if (sortedArray.length == 0) {
                    sortedArray = this.todo;
                    
                }
                sortedArray.sort((a, b) => {
                    return a.noteDate < b.noteDate;
                })
            }
            if (!this.sortByStatus && !this.sortByDate) {
                return this.todo;
            }

            return sortedArray;
        }
    }
});
notelist.refreshList();