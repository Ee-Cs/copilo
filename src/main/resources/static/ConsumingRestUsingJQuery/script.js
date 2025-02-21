$(document).ready(function() {
    $.ajax({
        url: "/company/departments/1"
    }).then(function(data) {
       $('.department-id').append("'").append(data.id).append("'");
       $('.department-name').append("'").append(data.name).append("'");
    });
});
