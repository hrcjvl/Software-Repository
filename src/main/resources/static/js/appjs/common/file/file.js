var prefix="/common/file"
$(function () {
    //添加界面的附件管理
   /* $('#file_upload').uploadify({
        'swf': '/Content/JQueryTools/uploadify/uploadify.swf',  //FLash文件路径
        'buttonText': '浏  览',                                 //按钮文本
        'uploader': '/FileUpload/Upload',                       //处理上传的页面
        'queueID': 'fileQueue',                         //队列的ID
        'queueSizeLimit': 1,                            //队列最多可上传文件数量，默认为999
        'auto': false,                                  //选择文件后是否自动上传，默认为true
        'multi': false,                                 //是否为多选，默认为true
        'removeCompleted': true,                        //是否完成后移除序列，默认为true
        'fileSizeLimit': '10MB',                        //单个文件大小，0为无限制，可接受KB,MB,GB等单位的字符串值
        'fileTypeDesc': 'Excel Files',                  //文件描述
        'fileTypeExts': '*.xls',                        //上传的文件后缀过滤器
        'onQueueComplete': function (event, data) {     //所有队列完成后事件
            //业务处理代码
            //提示用户Excel格式是否正常，如果正常加载数据
        },
        'onUploadStart': function (file) {
            InitUpFile();//上传文件前 ，重置GUID，每次不同
            $("#file_upload").uploadify("settings", 'formData', { 'folder': '数据导入文件', 'guid': $("#AttachGUID").val() }); //动态传参数
        },
        'onUploadError': function (event, queueId, fileObj, errorObj) {
            //alert(errorObj.type + "：" + errorObj.info);
        }
    });*/
    Load();
});
function Load(){
    $('#excelTable').bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : "/common/file", // 服务器数据的加载地址	//表格数据来源
                //	showRefresh : true,
                //	showToggle : true,
                //showColumns : true,
                iconSize : 'outline',
                toolbar : '#exampleToolbar',
                striped : true, // 设置为true会有隔行变色效果
                dataType : "json", // 服务器返回的数据类型
                pagination : true, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect : false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize : 10, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns : false, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams : function(params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        limit : params.limit,
                        offset : params.offset,
                        // name:$('#searchName').val(),
                        type : $('#searchName').val(),
                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns : [
                    {
                        checkbox : true
                    },
                    {
                        field : 'id',
                        title : '编号'//列
                    },
                    {
                        field : 'failure_number',
                        title : '失效序号'
                    },
                    {
                        field : 'time_to_fail',
                        title : '失效时间间隔'
                        //width : '100px'
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + row.id
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            var f = '<a class="btn btn-success btn-sm ' + s_add_h + '" href="#" title="增加"  mce_href="#" onclick="addD(\''
                                + row.type +'\',\''+row.description
                                + '\')"><i class="fa fa-plus"></i></a> ';
                            return e + d +f;
                        }
                    } ]
            });
}
function reLoad() {
    var opt = {
        query : {
            type : $('.chosen-select').val(),
        }
    }
    $('#excelTable').bootstrapTable('refresh', opt);
}

function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/add' // iframe的url
    });
}
function edit(id) {
    layer.open({
        type : 2,
        title : '编辑',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '600px', '320px' ],
        content : prefix + '/edit/' + id // iframe的url
    });
}
function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            url : prefix + "/remove",
            type : "post",
            data : {
                'id' : id
            },
            success : function(r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function addD(type,description) {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/add/'+type+'/'+description // iframe的url
    });
}
function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn : [ '确定', '取消' ]
        // 按钮
    }, function() {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function(i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type : 'POST',
            data : {
                "ids" : ids
            },
            url : prefix + '/batchRemove',
            success : function(r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function() {});
}

