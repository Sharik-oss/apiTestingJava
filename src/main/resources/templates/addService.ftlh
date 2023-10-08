<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Main Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <style>
        #postForm {
            display: none;
        }
    </style>
</head>
<body>

<div class="container">
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
<#--        <div class="col-md-3 mb-2 mb-md-0">-->
<#--            <a href="/" class="d-inline-flex link-body-emphasis text-decoration-none">-->
<#--                <img src="logo.png" alt="crocobet">-->
<#--            </a>-->
<#--        </div>-->

        <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
            <li><a href="/" class="nav-link px-2 link-secondary">Main</a></li>
            <li><a href="/dashboard" class="nav-link px-2 ">Dashboard</a></li>
            <li><a href="/services" class="nav-link px-2 ">Services</a></li>
        </ul>
    </header>
</div>

<form action="apis/add" method="post" enctype="multipart/form-data">
    <div id="getForm">
        <label for="name">Name</label>
        <input type="text" name="name" class="form-control"/><br><br>
        <label for="name">URL</label>
        <input type="text" name="url" class="form-control"><br><br>
    </div>

    <div id="postForm">
        <label for="name">Name</label>
        <input type="text" name="name" class="form-control"/><br><br>
        <label for="url">URL</label>
        <input type="text" name="url" class="form-control"><br><br>
        <label for="userName">User Name</label>
        <input type="text" name="userName" class="form-control"><br><br>
        <label for="password">Password</label>
        <input type="text" name="password" class="form-control"><br><br>
    </div>

    <button type="button" id="showPostForm" class="btn btn-primary">Enable post request</button>
    <input type="submit" value="Add" class="btn btn-primary"/>
</form>

<script>
    const showFormButton = document.getElementById("showPostForm");
    const postForm = document.getElementById("postForm");
    const getForm = document.getElementById("getForm");
    showFormButton.addEventListener("click", () => {
        if(getForm.style.display === "block"){
            postForm.style.display = "block";
            getForm.style.display = "none";
            console.log(getForm.style.display);
            showFormButton.textContent = "Disable post request";
        }else{
            postForm.style.display = "none";
            getForm.style.display = "block";
            console.log(getForm.style.display);
            showFormButton.textContent = "Enable post request";
        }
    });

    document.addEventListener("DOMContentLoaded", function () {
        const urlInputPostForm = document.querySelector('#postForm input[name="url"]');

        urlInputPostForm.addEventListener("keydown", function (event) {
            if (event.key === ',') {
                event.preventDefault();
                if (urlInputPostForm.value !== null) {
                    urlInputPostForm.value = urlInputPostForm.value.replace(',', '');
                }
            }
        });
    });




</script>

<footer class="py-3 my-4 bg-light fixed-bottom">
    <div class="container text-center">
        <p class="text-muted mb-0">© Sharik :) Version 0.0.1</p>
    </div>
</footer>
</body>
</html>