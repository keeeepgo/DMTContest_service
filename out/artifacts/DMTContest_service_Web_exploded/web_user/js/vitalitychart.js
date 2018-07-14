var myChart = echarts.init(document.getElementById('vitality'), 'macarons');

var url_VitalityChart = "http://localhost:8080/VitalityChart"
function getVitalityChartData() {
    $.ajax({
        url: url_VitalityChart,
        type: "GET",
        data: "userId="+userId,
        success: function(str){
            var format_getdata = [];
            var getdata = JSON.parse(str);
            for(i=0;i<getdata.length;i++){
                format_getdata[i] = [getdata[i]["date"],getdata[i]["newsAmount"]];
            }
            var option = {series:{data:format_getdata}}
            myChart.setOption(option);
        }
    });  
}

option = {
    title: {
        top: 30,
        left: 'center',
        text: '今年的活跃度表'
    },
    tooltip: {},
    visualMap: {
        min: 0,
        max: 50,
        type: 'piecewise',
        orient: 'horizontal',
        left: 'center',
        top: 65,
        textStyle: {
            color: '#000'
        }
    },
    calendar: {
        top: 120,
        left: 30,
        right: 30,
        cellSize: ['auto', 13],
        range: '2018',
        itemStyle: {
            normal: { borderWidth: 0.5 },
        },
        yearLabel: { show: false }
    },
    series: {
        type: 'heatmap',
        coordinateSystem: 'calendar',
        data: [],
        itemStyle: {
            normal: {
                color: '#aaa'
            }
        }
    }
};

myChart.setOption(option);
getVitalityChartData();