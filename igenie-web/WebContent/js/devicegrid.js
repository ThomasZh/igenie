function imageFormatter( cellvalue, options, rowObject ){
    return '<img src="'+cellvalue+'" />';
}

var jsonString = '{"rows":[{"createTime":"1408351167","lastUpdateTime":"1410765113","state":"91","deviceId":"0272881A-BBF2-4B23-9C11-8B0138128CA4","apnsToken":"","osVersion":"iOS:7.1"}]}'; 
var myData = eval("(" + jsonString + ")"); 

var mygrid = jQuery("#devicegrid").jqGrid({
   	url: 'account.go',
	datatype: "json",
	async: true,
   	colNames: ['Account ID','Login Name','State','Avatar','Account Name','Account Description','Avatar URL','Create Time'],
   	colModel: [
   		{name:'account_id',index:'account_id',width:225,editable:false,
   			editoptions:{readonly:true,size:10},
   			search:false
   		},
   		{name:'login_name',index:'login_name',width:180,align:"left",editable:true,
   			editoptions:{readonly:true,size:30},
   			formoptions:{rowpos:2,elmprefix:"(*)"}
   		},
   		{name:'state',index:'state',width:50,editable:true,
   			formatter:"select",edittype:"select",editoptions:{value:"62:Register;63:Active;64:Inactive"},
   			stype:'select',searchoptions:{sopt:['eq'],value:":All;62:Register;63:Active;64:Inactive"},
   			formoptions:{rowpos:3,elmprefix:"&nbsp;&nbsp;&nbsp;&nbsp;"}
   		},
   		{name:'avatar',index:'avatar_url',width:40,align:"left",search:false,
   			formatter:imageFormatter,edittype:'image',
   			editable:false,editoptions:{size:30},editrules:{edithidden:true, required:true},
   			formoptions:{rowpos:4,elmprefix:"&nbsp;&nbsp;&nbsp;&nbsp;"}
   		},
   		{name:'account_name',index:'account_name',width:160,align:"left",editable:true,
   			editoptions:{size:30},editrules:{edithidden:true, required:true},
   			formoptions:{rowpos:5,elmprefix:"(*)"}
   		},
   		{name:'account_desc',index:'account_desc',width:260,align:"left",editable:true,
   			editoptions:{size:30},editrules:{edithidden:true, required:true},
   			formoptions:{rowpos:6,elmprefix:"&nbsp;&nbsp;&nbsp;&nbsp;"}
   		},
   		{name:'avatar_url',index:'avatar_url',width:40,align:"left",hidden:true,search:false,
   			editable:true,editoptions:{size:30},editrules:{edithidden:true,required:true},
   			formoptions:{rowpos:7,elmprefix:"&nbsp;&nbsp;&nbsp;&nbsp;"}
   		},
   		{name:'create_time',index:'create_time',align:"left",
   			width:80, editable:true,edittype:'text',
   			editoptions:{readonly:true,size:30},
   			formatter:'date', formatoptions: {srcformat: 'U', newformat:'Y-m-d'},
   			editoptions:{
   				size:20, maxlengh:20,
   				dataInit: function(element) {
   	        		$(element).datepicker({dateFormat: 'yy-mm-dd'});
   	      		},
	   	      	defaultValue: getCurrentTime
   			},
   		    formoptions:{rowpos:8,elmprefix:"&nbsp;&nbsp;&nbsp;&nbsp;",elmsuffix:" yyyy-mm-dd" }
   		}
	],
   	rowNum: 10,
   	rowList: [10,20,30],
   	pager: '#pagernav',
   	sortname: 'create_time',
    viewrecords: true,
    jsonReader : {
        root: "rows",
        page: "page",
        total: "total",
        records: "records",
        repeatitems: false,
        id: "account_id",
		// instruct subgrid to get the data as name:value pair
//        subgrid: {
//  	      root:"rows", //子级的内容 Action类中必须有与之匹配的属性
//  	      repeatitems: true,  //false之后 subGridModel的mapping才起作用
//  	    }
    },
    subGrid: true, // set the subGrid property to true to show expand buttons for each row
//    subGridType: 'json', // set the subgrid type to json
//	subGridUrl: 'device.go?oper=query',
//	// description of the subgrid model
//	subGridModel:[{
//			name: ["deviceId","osVersion","apnsToken","createTime","lastUpdateTime","state"],
//			width: [225,180,250,100,100,100],
//			align: ["left","left","left","right","right","right"],
//			mapping: ["deviceId","osVersion","apnsToken","createTime","lastUpdateTime","state"] ,
//			params: false
//	}],
//	subGridOptions: {expandOnLoad: true},
//	gridComplete: function() {
//        var timeOut = 50;
//        var rowIds = $("#devicegrid").getDataIDs();
//        $.each(rowIds, function (index, rowId) {
//            if(rowId.row_cnt != 0){
//                setTimeout(function() {
//                    $("#devicegrid").expandSubGridRow(rowId);
//                }, timeOut);
//                timeOut = timeOut + 200;
//            }
//        });
//    },
    
	subGridRowExpanded:function(subgrid_id, row_id){
        var subgrid_table_id = subgrid_id+"_t";
        jQuery("#" + subgrid_id).html("<table id='"+subgrid_table_id+"'class='scroll'></table>"); 
        jQuery("#"+subgrid_table_id).jqGrid({
//             url:'device.go?oper=query&id='+row_id,
//             datatype: "json",
                datatype: 'local',
                data: myData,
//             mtype: 'POST',
             colNames:["deviceId","osVersion","apnsToken","createTime","lastUpdateTime","state"],
             colModel: [
                  {name:'deviceId',index:'deviceId',sortable:false,width:296,editable:false,align:"left"},
                  {name:'osVersion',index:'osVersion',sortable:true,width:253,editable:false,align:"right"},
                  {name:'apnsToken',index:'apnsToken',sortable:true,width:275,editable:false,align:"right"},
                  {name:'createTime',index:'createTime',sortable:true,width:270,editable:false,align:"right"},
                  {name:'lastUpdateTime',index:'lastUpdateTime',sortable:true,width:270,editable:false,align:"right"},
                  {name:'state',index:'state',sortable:true,width:270,editable:false,align:"right"}
            ],
            complete: function (jsondata, stat) {
//            	if (stat == "success") {
            	var thegrid = jQuery("#subgrid_table_id")[0],
            	data = eval("(" + jsondata.responseText + ")");
            	thegrid.addJSONData(data);
//            	}
            },
//			altRows: true,
//            shrinkToFit:true,
//            autowidth:true,
//            viewrecords: true, 
//            pager: subgrid_pager_id,  
            height: "100%",  
//            rowNum: 5,
//            sortname: 'create_time',
//            jsonReader: {   // 针对子表格的jsonReader设置  
//                root:"rows",  
//                repeatitems : false
//            }
         });
    },
    sortorder: "asc",
    caption:"Account Management",
    editurl:"account.go",
	height:280,
	hidegrid: false //Don't show the expand/collapse button on the top right
});

jQuery("#devicegrid").jqGrid('navGrid','#pagernav',
	{view:true,search:false}, //options
{   
	height:280,
	jqModal:true, 
	closeOnEscape:true, // use ESC close tab
	reloadAfterSubmit:false,
	bottominfo : "Fields marked with (*) are required"
}, // edit options
{   
	recreateForm:true,
    height:280,
    closeOnEscape:true, // use ESC close tab
    reloadAfterSubmit:true,
    bottominfo : "Fields marked with (*) are required"
}, // add options
{   
	mtype:"POST",
	reloadAfterSubmit:false
}, // del options
{
	multipleSearch:false
} // search options
);

jQuery("#devicegrid").jqGrid('navButtonAdd', "#pagernav", {
	caption: "",
	title: "Toggle Search Toolbar",
	buttonicon: 'ui-icon-pin-s',
	onClickButton: function() {
		mygrid[0].toggleToolbar();
	}
});

jQuery("#devicegrid").jqGrid('filterToolbar',{
	stringResult: true,
	searchOnEnter: false,
	autosearch: true
});