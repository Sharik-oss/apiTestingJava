<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
<#--        <div class="col-md-3 mb-2 mb-md-0">-->
<#--            <a href="/" class="d-inline-flex link-body-emphasis text-decoration-none">-->
<#--                <svg class="bi" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"></use></svg>-->
<#--            </a>-->
<#--        </div>-->

        <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
            <li><a href="/" class="nav-link px-2 link-secondary">Main</a></li>
            <li><a href="/dashboard" class="nav-link px-2 ">Dashboard</a></li>
            <li><a href="/services" class="nav-link px-2 ">Services</a></li>
        </ul>
    </header>
</div>
<a class="btn btn-danger" href="add" >Add</a>
<table class="table table-striped-columns">
    <tr>
        <th>Name</th>
        <th>Url</th>
        <th>Edit</th>
    </tr>
    <#list apiService as apiservice>
        <tr>
            <td>${apiservice.name}</td>
            <td>${apiservice.url}</td>
            <td>
                <div class="d-flex align-items-center">
                    <a href="#" class="btn btn-danger me-2" onclick="replaceCommaForEditButton(event)" data-id="${apiservice.id}">Edit</a>
                    <form action="/apis/delete/${apiservice.id}" method="post" class="inherited-list" data-id="${apiservice.id}">
                        <input type="hidden" name="_method" value="delete">
                        <button type="button" class="btn btn-danger" onclick="replaceCommaAndSubmit(event)">Delete</button>
                    </form>
                </div>
            </td>
        </tr>
    </#list>
</table>

<script>
    function replaceCommaForEditButton(event) {
        event.preventDefault();
        var link = event.target;
        var id = link.getAttribute("data-id").replace(/,/g, "");
        window.location.href = "/api/" + id;
    }
    function replaceCommaAndSubmit(event) {
        event.preventDefault();
        var button = event.target;
        var form = button.closest('form');
        var id = form.getAttribute("data-id").replace(/,/g, "");
        form.action = "/apis/delete/" + id;
        form.submit();
    }
</script>
<footer class="py-3 my-4 bg-light fixed-bottom">
    <div class="container text-center">
        <p class="text-muted mb-0">© Sharik :) Version 0.0.1</p>
    </div>
</footer>
</body>
</html>