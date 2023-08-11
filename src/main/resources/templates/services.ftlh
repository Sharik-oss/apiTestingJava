<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
        <div class="col-md-3 mb-2 mb-md-0">
            <a href="/" class="d-inline-flex link-body-emphasis text-decoration-none">
                <svg class="bi" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"></use></svg>
            </a>
        </div>

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
                    <a href="/api/${apiservice.id}" class="btn btn-danger me-2">Edit</a>
                    <form action="/apis/delete/${apiservice.id}" method="post" class="inherited-list">
                        <input type="hidden" name="_method" value="delete">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </td>
        </tr>
    </#list>
</table>

</body>
</html>