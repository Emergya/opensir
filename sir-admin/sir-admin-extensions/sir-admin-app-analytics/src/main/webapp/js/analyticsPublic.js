
/**
 * Javascript for the analytics module public area.
 */
var analytics = {
    toggleSearchForm: function() {
        $('#searchFormCtr').toggleClass('hidden', 300);
    },
    viewDetails : function(name,url) {
        loadContentDialog("viewDetailsPopup", name, url);
    },
    init: function() {

        $.fn.tagcloud.defaults = {
            size: {
                start: 10,
                end: 14,
                unit: 'pt'
            },
            color: {
                start: '#086db3',
                end: '#ED4044'
            }
        };

        $('#categoriesCloud a').tagcloud();
        $('#tagsCloud a').tagcloud();

        // with jQuery
        $('#dataItemsCtr').masonry({
        	layoutPriorities : {
        		upperPosition:0,
        		shelfOrder: 10000000000
        	},
        	itemSelector: '.datumPreview',
            isAnimated: true
        });
        
        $("#dataItemsCtr").infinitescroll({
        	appendCallback: false,
        	nextSelector: ".loadMoreCtr a.loadMore:last",
        	navSelector: ".loadMoreCtr",
        	itemSelector: ".datumPreview",        	
        	pathParse: function(path) {      
        		path = path.match("^(.*)[0-9]+").slice(1);
        		path.push("");
        		return path;
        	}
        },function(elements, opts) {
        	if(elements.length) {
        		$('#dataItemsCtr').masonry().append(elements).masonry("appended",elements).masonry();
        	} else {
        		opts.state.isDone = true;
        	}
        	
        });
    }
};

$(document).ready(function() {
    analytics.init();
});