# Leave-Management-Webapp

<h2>Overview</h2>
Leave Management WebApp - This project is designed to simplify the process of managing employee leave requests. With an simple & clean user interface, the app provides different dashboards and views based on the user's role, ensuring that both employees and managers can efficiently handle their leave requests and team activities.
<hr>

<h2>Features</h2>
<h4>For Employees</h4>
<ul>
  <li><strong>Personal Dashboard:</strong> Employees can view and manage their own leave requests, track leave balances</li>
  <li><strong>Leave Request Form:</strong> Easily submit leave requests and view their current status. The leave types were also arranged based on their gender.If the employee has already reached the leave limit then he/she won't get the option of choosing that leave type and also while applying leave employee can check for the remaining number of leaves for the employee. Also the days will not be calculated for weekends and holidays</li>
  <li><strong>Leave Management: </strong> They can view their leave requests based on the leave status (Approved, Rejected, Pending)</li>
</ul>
<h4>For Managers</h4>
<ul>
  <li><strong>Team Dashboard: </strong> Managers have access to a comprehensive dashboard showing the employees leaves details.</li>
  <li><strong>Team Leave Table: </strong> View a detailed table of all leave requests within the team, including pending, approved, and rejected requests. Also while changing the leave status the manager can see the employee leave history details</li>
</ul>

<h2>Additional Features</h2>
<ul>
  <li>If the user try to access the home page without logging in the page redirects to login page (used webfilter)</li>
  <li>After user logged in a cookiee with name "unqId" will be created and it will be stored untill the user logout.</li>
  <li>While calculating the leaves, weekends and holidays are excluded, also the employee can't pick a holiday/weekend/past date while applying for leave.</li>
  <li>Maternity & Paternity leaves were assigned based on gender</li>
  <li>Dialog box appears everytime while applying/approving/rejecting the leave</li>
</ul>
<hr>

<h3>Login Page</h3>

![Screenshot (48)](https://github.com/user-attachments/assets/7ed5fd12-7020-4f23-bf23-5a11b0a09bcb)

<h3>Employee Dashboard <span>(If employee is not a manager then he won't get the team dashboard button)</span></h3>

![Screenshot (49)](https://github.com/user-attachments/assets/7173efc5-d2a9-4b20-a971-fd0ead3920e7)


<h3>Team Dashboard (only for managers)</h3>

![Screenshot (50)](https://github.com/user-attachments/assets/0f6486da-cb29-4c33-a465-acee057c0aef)

<h3>Leave Applying form</h3>

![Screenshot (53)](https://github.com/user-attachments/assets/86433d50-71cd-4d1c-82ec-68140af746cd)

<h3>Dialog box appears if we choose any invalid date</h3>

![Screenshot (54)](https://github.com/user-attachments/assets/1778d516-611d-4689-b516-1145f846b37e)

<h3>Dialog box while applying for leave</h3>

![Screenshot (55)](https://github.com/user-attachments/assets/f023a866-3490-48e4-866a-1513e7e35bbd)

<h3>Employees Leaves Table - (Pending/Approved/Rejected/All)</h3>

![Screenshot (56)](https://github.com/user-attachments/assets/35d3f2aa-59e6-450b-be34-1538e6ffc7c5)

<h3>Managers Leaves Table for Approving/Rejecting the leave</h3>

![Screenshot (62)](https://github.com/user-attachments/assets/d737bbea-be0f-481a-b83a-49268294cd74)


<h3>On Clicking view more he can see details of employee while approving/rejecting the leave</h3>

![Screenshot (58)](https://github.com/user-attachments/assets/6c00ccc2-93a9-4a68-b086-36d7f0bf2f13)

<h3>Dialog box after approving/ rejecting the leave</h3>

![Screenshot (59)](https://github.com/user-attachments/assets/b7f17e20-755e-4b84-8061-e7d4a08ef7f6)


<h3>Holidays table</h3>

![Screenshot (60)](https://github.com/user-attachments/assets/577052f3-12ab-4941-9605-81e032714d51)


<h3>Logout profile section on navbar</h3>

![Screenshot (61)](https://github.com/user-attachments/assets/3ad680da-f6ab-4e6b-9a8b-ad1b63e74371)
