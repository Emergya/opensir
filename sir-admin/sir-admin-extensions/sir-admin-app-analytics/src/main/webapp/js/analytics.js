$(document).ready(function() {
    $('.tagsField').tagit({
        allowSpaces: true,
        autocomplete: {
            delay: 200,
            minLenght: 2,
            source: function(request, response) {
                $.getJSON(urls.analyticsTagsSearchUrl, {
                    "searchTerm": request.term
                }, response);
            }
        }
    });
});
