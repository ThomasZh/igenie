function imageFormatter( cellvalue, options, rowObject ){
    return '<img src="'+cellvalue+'" />';
}

var mygrid = jQuery("#usergrid").jqGrid({
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
        cell: "cell",
        id: "account_id"
    },
    sortorder: "asc",
    caption:"Account Management",
    editurl:"account.go",
	height:280,
	hidegrid: false //Don't show the expand/collapse button on the top right
});

jQuery("#usergrid").jqGrid('navGrid','#pagernav',
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

jQuery("#usergrid").jqGrid('navButtonAdd', "#pagernav", {
	caption: "",
	title: "Toggle Search Toolbar",
	buttonicon: 'ui-icon-pin-s',
	onClickButton: function() {
		mygrid[0].toggleToolbar();
	}
});

jQuery("#usergrid").jqGrid('filterToolbar',{
	stringResult: true,
	searchOnEnter: false,
	autosearch: true
});