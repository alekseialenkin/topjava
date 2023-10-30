const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " + date.substring(11, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data['excess']);
            }
        })
    );

});

$.ajaxSetup({
    converters: {
        "text json": function (result) {
            var newResult = JSON.parse(result);
            if (newResult.type === 'object') {
                $(newResult).each(function () {
                    this.dateTime = this.dateTime.substring(0, 16).replace("T", " ");
                });
            }
            return newResult;
        }
    }
});

$('#startDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    formatDate: 'Y-m-d'
});

$('#endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    formatDate: 'Y-m-d'
});

$('#startTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
    formatTime: 'H:i'
});

$('#endTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
    formatTime: 'H:i'
})

$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
})

