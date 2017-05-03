/**
 * Controls the star rating selectors for surveys
 */

$('#response_01 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_01 select').barrating('clear');
        } else {
            $('#response_01 .null-warning').addClass('hidden');
            $('#response_01 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_02 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_02 select').barrating('clear');
        } else {
            $('#response_02 .null-warning').addClass('hidden');
            $('#response_02 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_03 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_03 select').barrating('clear');
        } else {
            $('#response_03 .null-warning').addClass('hidden');
            $('#response_03 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_04 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_04 select').barrating('clear');
        } else {
            $('#response_04 .null-warning').addClass('hidden');
            $('#response_04 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_05 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_05 select').barrating('clear');
        } else {
            $('#response_05 .null-warning').addClass('hidden');
            $('#response_05 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_06 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_06 select').barrating('clear');
        } else {
            $('#response_06 .null-warning').addClass('hidden');
            $('#response_06 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_07 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_07 select').barrating('clear');
        } else {
            $('#response_07 .null-warning').addClass('hidden');
            $('#response_07 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_08 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_08 select').barrating('clear');
        } else {
            $('#response_08 .null-warning').addClass('hidden');
            $('#response_08 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_09 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_09 select').barrating('clear');
        } else {
            $('#response_09 .null-warning').addClass('hidden');
            $('#response_09 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});

$('#response_10 select').barrating({
    theme: 'fontawesome-stars-o',
    showSelectedRating: false,
    allowEmpty: false,
    initialRating: null,
    fastClicks: true,
    onSelect: function(value, text) {
        if (!value) {
            $('#response_10 select').barrating('clear');
        } else {
            $('#response_10 .null-warning').addClass('hidden');
            $('#response_10 .your-rating').removeClass('hidden').find('span').html(value);
        }
    }
});
