window.onload = employeeDetails;

var manager = false;
var gender = "Male";
// var holidayList = [];



async function showSection(sectionId) {
    const sections = document.querySelectorAll('.section-content');
    sections.forEach(section => section.style.display = 'none');


    const selectedSection = document.getElementById(sectionId);
    selectedSection.style.display = 'block';

    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => link.classList.remove('active'));
    event.target.classList.add('active');


    if(sectionId==='leaveManagement'){
        const buttons = document.querySelectorAll('.filter-btn');
        
        buttons.forEach(btn => btn.classList.remove('active'));
        
        document.getElementById("myPendingLeavesBtn").classList.add('active');
        document.getElementById("myTeamPendingLeavesBtn").classList.add('active');

        await getMyLeaves("Pending");
        await myTeamLeavesByStatus("Pending");
    }
}


function setMinDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const minDate = `${year}-${month}-${day}`;

    document.getElementById('fromDate').setAttribute('min', minDate);
    document.getElementById('toDate').setAttribute('min', minDate);
}


function formatDate(dateString) {
    
    const date = new Date(dateString);

    
    const months = [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    
    const day = date.getDate();
    const month = months[date.getMonth()];
    const year = date.getFullYear();

    
    function getDayWithSuffix(day) {
        if (day > 3 && day < 21) return day + 'th';
        switch (day % 10) {
            case 1: return day + 'st';
            case 2: return day + 'nd';
            case 3: return day + 'rd';
            default: return day + 'th';
        }
    }

    return `${getDayWithSuffix(day)} ${month} ${year}`;
}


// leave management
document.addEventListener('DOMContentLoaded', function() {
    const myLeavesBtn = document.getElementById('myLeavesBtn');
    const myTeamLeavesBtn = document.getElementById('myTeamLeavesBtn');
    const myLeavesTable = document.getElementById('myLeavesTable');
    const myTeamLeavesTable = document.getElementById('myTeamLeavesTable');

    myLeavesBtn.addEventListener('click', function() {
        myLeavesTable.classList.remove('d-none');
        myTeamLeavesTable.classList.add('d-none');
        myLeavesBtn.classList.add('btn-active');
        myLeavesBtn.classList.remove('btn-inactive');
        myTeamLeavesBtn.classList.add('btn-inactive');
        myTeamLeavesBtn.classList.remove('btn-active');
    });

    myTeamLeavesBtn.addEventListener('click', function() {
        myLeavesTable.classList.add('d-none');
        myTeamLeavesTable.classList.remove('d-none');
        myLeavesBtn.classList.add('btn-inactive');
        myLeavesBtn.classList.remove('btn-active');
        myTeamLeavesBtn.classList.add('btn-active');
        myTeamLeavesBtn.classList.remove('btn-inactive');
    });

    // dashboard
    const myDBBtn = document.getElementById('myDBBtn');
    const myTeamDBBtn = document.getElementById('myTeamDBBtn');

    myDBBtn.addEventListener('click', function(){
        myDBBtn.classList.add('btn-active');
        myDBBtn.classList.remove('btn-inactive');
        myTeamDBBtn.classList.add('btn-inactive');
        myTeamDBBtn.classList.remove('btn-active');
        getAllLeaveTypes('my');
    });

    myTeamDBBtn.addEventListener('click', function(){
        myTeamDBBtn.classList.add('btn-active');
        myTeamDBBtn.classList.remove('btn-inactive');
        myDBBtn.classList.add('btn-inactive');
        myDBBtn.classList.remove('btn-active');
        getAllLeaveTypes('team');
    });

});







// profile
function toggleProfileBox() {
    const profileBox = document.getElementById('profileBox');
    if (profileBox.style.display === 'none' || profileBox.style.display === '') {
        profileBox.style.display = 'block';
    } else {
        profileBox.style.display = 'none';
    }
}











// ------------------------------------------------------------------------

// dialog
function openDialog(dialogId){
    console.log("inside dialog")
    document.getElementById(dialogId).showModal();
}

function closeDialog(dialogId){
    document.getElementById(dialogId).close();
}









// ------------logout----------------------------------------
async function logout(){
   const response = await fetch("logout");
   const result = await response.json();

   if(result.message=="logout"){
       window.location.href = "index.html";
   }else{
       console.log("error");
   }
}


// --------------employee details-----------------------------
async function employeeDetails() {
   const response = await fetch("employee?action=employeeDetails", {
       method : 'GET',
       headers : {
           'Content-Type' : 'application/json'
       }
   });

   const data = await response.json();
   console.log(data);

   document.getElementById("employeeName").innerHTML = data.employeeName;
   document.getElementById("employeePhone").innerHTML = data.employeePhone;
   document.getElementById("employeeEmailId").innerHTML = data.emailId;
   gender = data.employeeGender;
   if(gender=="Male"){
    document.getElementById("maternity-leave").remove();
    console.log("male");
   }
   if (gender=="Female"){
    document.getElementById("paternity-leave").remove();
    console.log("female");
   }
   isManager();
   getAllLeaveTypes('my');
   getAllHolidays();
   setMinDate();
}



// ----------------------------------is manager------------------------------------
async function isManager(){

   const response = await fetch("employee?action=isManager", {
       method : 'GET',
       headers : {
           'Content-Type' : 'application/json'
       }
   });

   const result = await response.json();
   if(result.message=='login'){
       console.log("login");
   }else if(result.message=='true'){
        manager = true;
       console.log("manager");
   }else if(result.message=='false'){
        manager = false;
       document.getElementById("myTeamLeavesBtn").style.display = "none";
       document.getElementById("myTeamDBBtn").style.display = "none";
    //    document.getElementById("myTeamLeavesNavLink").style.display = "none";
   }else{
       console.log("invalid");
   }
}




//----------------------------------- apply leave---------------------------------------------------------------------------

document.getElementById("applyLeaveBtn").addEventListener('click', async function(){

//    event.preventDefault();

   const leaveType = document.getElementById('leaveType').value;
   const fromDate = document.getElementById('fromDate').value;
   const toDate = document.getElementById('toDate').value;
   const leaveReason = document.getElementById('leaveReason').value;
   console.log(leaveType+fromDate+toDate+leaveReason);



   const response = await fetch("myLeaves", {
       method : 'POST',
       headers : {
           'Content-Type' : 'application/json'
       },
       body : JSON.stringify({
           leaveType : leaveType,
           fromDate : fromDate,
           toDate : toDate,
           leaveReason : leaveReason
       })
   })

   const result = await response.json();

//    alert("leave applied");
   openDialog('leaveAppliedDialog');
   document.getElementById('applyLeaveFrom').reset();
   document.getElementById('leaveType').value = '';
   document.getElementById('fromDate').value = '';
   toDate = document.getElementById('toDate').value = '';
   leaveReason = document.getElementById('leaveReason').value = '';

   if(result.status!='ok'){
       console.log("error");
   }

});






// ---------------------------------leave management ------my leaves---------------------------
function setActive(button, request) {

    if(request=="myLeaves"){

        const buttons = document.getElementById('myLeavesTable').querySelectorAll('.filter-btn');
        
        buttons.forEach(btn => btn.classList.remove('active'));
        
        button.classList.add('active');

        getMyLeaves(button.textContent);
    }else{
        
        const buttons = document.getElementById('myTeamLeavesTable').querySelectorAll('.filter-btn');
        
        buttons.forEach(btn => btn.classList.remove('active'));
        
        button.classList.add('active');

        myTeamLeavesByStatus(button.textContent);

    }
}


async function getMyLeaves(status){
    let response;
    if(status==="Pending"){
        response = await fetch("myLeaves?action=myPendingLeaveRequests");
    }else if(status==="Rejected"){
        response = await fetch("myLeaves?action=myRejectedLeaveRequests");
    }else if(status==="Approved"){
        response = await fetch("myLeaves?action=myApprovedLeaveRequests");
    }else if(status==="All"){
        response = await fetch("myLeaves?action=myLeaveRequests");
    }
    else{
        console.log("error");
        return;
    }
   
   console.log("get my leaves")
   const data = await response.json();
   console.log(data);

   const result = data;

   const tableBody = document.getElementById("myLeavesTableBody");
   tableBody.innerHTML = '';
   result.forEach(leaveRequest => {
       const newRow = document.createElement('tr');
       newRow.id = leaveRequest.leaveId;

       newRow.innerHTML = `
           <td>${leaveRequest.leaveType}</td>
           <td>${formatDate(leaveRequest.fromDate)}</td>
           <td>${formatDate(leaveRequest.toDate)}</td>
           <td>${leaveRequest.daysCount}</td>
           <td>${leaveRequest.leaveReason}</td>
           <td>${leaveRequest.leaveStatus}</td>
       `;

       tableBody.appendChild(newRow);
   });

}



// -------------------------leave management---------------------my team leaves------------------------------------
async function myTeamLeavesByStatus(status){
    let response;
    if(status==="Pending"){
        response = await fetch("myTeamLeaves?action=Pending");
    }else if(status==="Rejected"){
        response = await fetch("myTeamLeaves?action=Rejected");
    }else if(status==="Approved"){
        response = await fetch("myTeamLeaves?action=Approved");
    }else if(status==="All"){
        response = await fetch("myTeamLeaves?action=All");
    }else{
        console.log("error");
        return;
    }

   

   console.log(response);
   const result = await response.json();
   console.log(result);
   console.log(typeof result);
//    return;

   const tableBody = document.getElementById("myTeamLeavesTableBody");
   tableBody.innerHTML = '';
   result.forEach(leaveDTO => {
       const newRow = document.createElement('tr');
       newRow.id = leaveDTO.leaveId;

       if(leaveDTO.leaveStatus=="Pending"){
        newRow.innerHTML = `
               <td>${leaveDTO.employeeName}</td>
               <td>${leaveDTO.leaveType}</td>
               <td>${formatDate(leaveDTO.fromDate)}</td>
               <td>${formatDate(leaveDTO.toDate)}</td>
               <td>${leaveDTO.daysCount}</td>
               <td>${leaveDTO.leaveReason}</td>
               <td>
                    <button class="btn btn-success btn-sm me-2" onclick="changeLeaveStatus('${leaveDTO.leaveId}', 'Approved',this)">Approve</button>
                    <button class="btn btn-danger btn-sm me-2" onclick="changeLeaveStatus('${leaveDTO.leaveId}' , 'Rejected', this)">Reject</button>
                    <a href="#" class="btn btn-link btn-sm" onclick="showEmpLeaveDetails('${leaveDTO.employeeEmailId}', '${leaveDTO.leaveId}')">View More</a>
                </td>
           `; 
       }else{
        newRow.innerHTML = `
               <td>${leaveDTO.employeeName}</td>
               <td>${leaveDTO.leaveType}</td>
               <td>${formatDate(leaveDTO.fromDate)}</td>
               <td>${formatDate(leaveDTO.toDate)}</td>
               <td>${leaveDTO.daysCount}</td>
               <td>${leaveDTO.leaveReason}</td>
               <td>
                    <span>${leaveDTO.leaveStatus}</span>
                    
                </td>
           `; 
       }

       tableBody.appendChild(newRow);
   });

}







// ---------------------------------------change leave status -------------my team leaves-------------

async function changeLeaveStatus(leaveId, leaveStatus, button){
    const response = await fetch("myTeamLeaves", {
            method : 'PUT',
            headers : {
                'Content-Type' : 'application/json'
            },
            body : JSON.stringify({
                leaveId : leaveId,
                leaveStatus : leaveStatus
            })
        });
//    alert('leave' + message );
    // document.getElementById(leaveId).remove();

    const name = document.getElementById(leaveId).getElementsByTagName("td")[0].textContent;
    const leaveType = document.getElementById(leaveId).getElementsByTagName("td")[1].textContent;

    if(document.getElementById("myTeamPendingLeavesBtn").classList.contains('active')){
        document.getElementById(leaveId).remove();
    }else{
        button.parentElement.innerHTML = `
                    <span>${leaveStatus}</span>
           `;
    }
    document.getElementById("leaveStatusDialog-text").textContent = `${leaveType} ${leaveStatus} for ${name}`;
    openDialog('leaveStatusDialog');
}



async function getAllLeaveTypes(message) {

    leaveTypeAddOns = {
        "Casual Leave" : {"styling" : "bg-success"},
        "Annual Leave" : {"styling" : "bg-danger"},
        "Paid Leave" : {"styling" : "bg-info"},
        "Sick Leave" : {"styling" : "bg-primary"},
        "Maternity Leave" : {"styling" : "bg-warning"},
        "Paternity Leave" : {"styling" : "bg-warning"},
    }

    let response;
    if(message=="my"){
        response = await fetch("leaveType?action=allLeavesType");
    }else{
        response = await fetch("leaveType?action=allTeamLeavesType");
    }
    
    const result = await response.json();

    const dashboardBody = document.getElementById("leave-types-dashboard");
    dashboardBody.innerHTML = '';
    result.forEach(leaveTypeDTO=> {

        if(message=="my" && ((gender=="Male" && leaveTypeDTO.leaveType=="Maternity Leave") || (gender=="Female" && leaveTypeDTO.leaveType=="Paternity Leave"))){
            return;
        }

        const newDiv = document.createElement("div");
        newDiv.className = "col-md-4";
        newDiv.id = leaveTypeDTO.leaveType;
        const leaveTypeStyling = leaveTypeAddOns[leaveTypeDTO.leaveType].styling;
        newDiv.innerHTML = `
                <div class="card mb-3 shadow-sm">
                    <div class="card-header text-white ${leaveTypeStyling}">${leaveTypeDTO.leaveType}</div>
                    <div class="card-body">
                        <h5 class="card-title">Total Days: ${leaveTypeDTO.leaveLimit}</h5>
                        <p class="card-text">Taken: ${leaveTypeDTO.daysCount} days</p>
                        <p class="card-text">Remaining: ${leaveTypeDTO.leaveLimit - leaveTypeDTO.daysCount} days</p>
                    </div>
                </div>
            `;

        dashboardBody.appendChild(newDiv);
    })
}





// -----------------------------holidaysss-----------------------------------------

async function getAllHolidays(){
    const response = await fetch("leaveType?action=allHolidays");
    const result = await response.json();

    const tableBody = document.getElementById("holidaysTableBody");
    tableBody.innerHTML = '';
    result.forEach(holiday => {
        // holidayElement = {from : holiday.holidayFromDate, to: holiday.holidayToDate};
        // holidayList.push(holidayElement);

        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${holiday.holidayName}</td>
            <td>${formatDate(holiday.holidayFromDate)}</td>
            <td>${formatDate(holiday.holidayToDate)}</td>
            <td>${holiday.daysCount}</td>
            <td>${holiday.holidayType}</td>
        `;
        tableBody.appendChild(newRow);
        // console.log(holiday.fromDate);

    });
    console.log(holidayList);
    // initializeDatePickers();
}


// function initializeDatePickers() {
//     flatpickr("#fromDate", {
//         disable: holidayList,
//         dateFormat: "Y-m-d",
//     position: "below",
//     appendTo: document.body,
//     });

//     flatpickr("#toDate", {
//         disable: holidayList,
//         dateFormat: "Y-m-d",
//     position: "below",
//     appendTo: document.body,
//     });
// }












// --------------------leave dialog---------------------
async function showEmpLeaveDetails(emailId, leaveId) {
    console.log("indisde show emp details");
    openDialog("leaveDialog");

    const tableRow = document.getElementById(leaveId);
    

    document.getElementById("leaveDialog-empName").textContent = tableRow.getElementsByTagName("td")[0].textContent;
    document.getElementById("leaveDialog-empMail").textContent = emailId;
    document.getElementById("leaveDialog-leaveType").textContent = tableRow.getElementsByTagName("td")[1].textContent;
    document.getElementById("leaveDialog-fromDate").textContent = tableRow.getElementsByTagName("td")[2].textContent;
    document.getElementById("leaveDialog-toDate").textContent = tableRow.getElementsByTagName("td")[3].textContent;
    document.getElementById("leaveDialog-leaveReason").textContent = tableRow.getElementsByTagName("td")[5].textContent;
    // document.getElementById("leaveDialog-leaveStatus").textContent = tableRow.getElementsByTagName("td")[6].textContent;


    const response = await fetch(`leaveType?action=allLeavesType&mail=${emailId}`);
    const result = await response.json();

    const tableBody = document.getElementById("leaveDialog-tableBody");
    tableBody.innerHTML = '';
    result.forEach(leaveTypeDTO=> {
        if(((gender=="Male" && leaveTypeDTO.leaveType=="Maternity Leave") || (gender=="Female" && leaveTypeDTO.leaveType=="Paternity Leave"))){
            return;
        }
        const newRow = document.createElement("tr");
        
        newRow.innerHTML = `
            <td>${leaveTypeDTO.leaveType}</td>
            <td>${leaveTypeDTO.daysCount}</td>
            <td>${leaveTypeDTO.leaveLimit - leaveTypeDTO.daysCount}</td>
        `;
        tableBody.appendChild(newRow);
    });
}