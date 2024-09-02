document.getElementById("loginForm").addEventListener("submit", async function(event){
    event.preventDefault();

    const emailID = document.getElementById("emailID").value;
    const password = document.getElementById("password").value;

    const params = new URLSearchParams();
        params.append('emailID', emailID);
        params.append('password', password);

        const response = await fetch('login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
    try{
    const result = await response.json();
    console.log(result);
    if (result.status == 'error'){
        console.log("inside if");
        document.getElementById("error-message").style.display = "block";
    }else{
        console.log("logged in");
        window.location.href = "home.html";
    }}
    catch(error){
        console.log("error")
    }

})