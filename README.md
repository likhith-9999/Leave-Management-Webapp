# Leave-Management-Webapp

<h2>Overview</h2>
Leave Management WebApp - This project is designed to simplify the process of managing employee leave requests. With an simple & clean user interface, the app provides different dashboards and views based on the user's role, ensuring that both employees and managers can efficiently handle their leave requests and team activities.
<hr>

<h2>Features</h2>
<h4>For Employees</h4>
<ul>
  <li><strong>Personal Dashboard:</strong> Employees can view and manage their own leave requests, track leave balances</li>
  <li><strong>Leave Request Form:</strong> Easily submit leave requests and view their current status. The leave types were also arranged based on their gender. Also the days will not be calculated for weekends and holidays</li>
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
  <li>While calculating the leaves, weekends and holidays are excluded, also the employee can't pick a past date while applying for leave.</li>
  <li>Maternity & Paternity leaves were assigned based on gender</li>
</ul>
<hr>





