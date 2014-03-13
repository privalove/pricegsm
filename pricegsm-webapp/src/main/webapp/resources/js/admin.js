(function () {
    'use strict';

    $('.confirm-delete').on('click', function (e) {
        e.preventDefault();

        var action = $(this).data('action');

        $("<form action='" + action + "' method='post'><input type='hidden' name='_method' value='delete'></form>").submit();
    });

    $('a[data-delete-action]').on('click', function (e) {
        e.preventDefault();

        $('.confirm-delete').data('action', $(this).data('delete-action'));
    });

    $('.tagsInput').each(function () {
        $(this).tagsInput({
            width: 'auto'
        });
    });

    $('.color-picker').colorpicker({
        format: 'hex'
    });
    $('.color-picker-rgba').colorpicker({
        format: 'rgba'
    });
    $('.colorpicker-component').colorpicker();


})();

function filterByVendor(location) {
    document.location = location
}