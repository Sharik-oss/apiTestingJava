<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
<#--        <div class="col-md-3 mb-2 mb-md-0">-->
<#--            <a href="/" class="d-inline-flex link-body-emphasis text-decoration-none">-->
<#--                <svg class="bi" width="40" height="32" role="img" aria-label="Bootstrap">-->
<#--                    <use xlink:href="#bootstrap"></use>-->
<#--                </svg>-->
<#--            </a>-->
<#--        </div>-->

        <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
            <li><a href="/" class="nav-link px-2 link-secondary">Main</a></li>
            <li><a href="/dashboard" class="nav-link px-2 ">Dashboard</a></li>
            <li><a href="/services" class="nav-link px-2 ">Services</a></li>
        </ul>
    </header>
</div>
<form action="/apis/update/${api.id?replace(",", "")}" method="post" enctype="application/x-www-form-urlencoded" accept-charset="UTF-8">
    <div class="form-group">
        <label for="exampleInputEmail1">Name:</label>
        <input type="text" id="form" class="form-control" name="name" value="${api.name}" disabled/>
    </div>
    <br>
    <div class="form-group">
        <label for="exampleInputEmail1">URL</label>
        <input type="text" id="form1" class="form-control" name="url" value="${api.url}" disabled/>
    </div>
    <br>
    <button type="button" id="enableButton" class="btn btn-primary btn-sm">Turn on edit mode</button>
    <input type="hidden" name="_method" value="PUT">
    <button id="updateForm" type="submit">Update API</button>
</form>

<script type="text/javascript">
    const btn = document.getElementById('enableButton');
    const form = document.getElementById('form');
    const form1 = document.getElementById('form1');

    btn.addEventListener('click', () => {
        if (!form.disabled) {
            form.value = form.value.replace(/,/g, ''); // Remove commas
            form.disabled = true;
        } else {
            form.disabled = false;
        }
        if (!form1.disabled) {
            form1.value = form1.value.replace(/,/g, ''); // Remove commas
            form1.disabled = true;
        } else {
            form1.disabled = false;
        }
    });
</script>

<footer class="py-3 my-4 bg-light fixed-bottom">
    <div class="container text-center">
        <p class="text-muted mb-0">© Sharik :) Version 0.0.1</p>
    </div>
</footer>
</body>
</html>
