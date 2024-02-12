<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>Customer List</title>
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
   <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<body>
<div class="container mt-2 ">

   <h4>Customer List</h4>

   <!-- search dropdown and input -->
   <div class="row mt-3 mb-3 w-50 ">
      <div class="col-md-4">
         <select class="form-control" id="searchCriteria">
            <option value="firstName">First Name</option>
            <option value="email"> Email</option>
            <option value="phone">Phone</option>
            <option value="city">City</option>
         </select>
      </div>
      <div class="col-md-4">
         <input type="text" class="form-control" id="searchValue" placeholder="Search" onkeyup="searchCustomers()">
      </div>

      <div class="col-md-4">
         <button type="button" class="btn btn-primary  ml-5" id="syncBtn" onclick="sync()">Sync</button>
      </div>
   </div>

   <%-- Showing Customer List Table  --%>
   <div class="table-responsive">
      <table class="table table-bordered table-striped">
         <thead class="thead-dark">
         <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Address</th>
            <th>Street</th>
            <th>City</th>
            <th>State</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Action</th>
         </tr>
         </thead>
         <tbody id="customerTableBody"></tbody>
      </table>
   </div>

   <%--Pagination --%>
   <nav aria-label="Page navigation">
      <ul class="pagination" id="pagination"></ul>
   </nav>

   <!-- Add/Edit Customer Form -->
   <div class="w-50 text-center m-auto">
      <p>Add/Update Customer</p>
      <form id="customerForm">
         <div class="row">
            <!-- First Name and Last Name -->
            <div class="col-md-6">
               <div class="form-group">
                  <input type="text" class="form-control" id="firstName" name="firstName" placeholder="First Name"
                         required>
               </div>
            </div>
            <div class="col-md-6">
               <div class="form-group">
                  <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Last Name"
                         required>
               </div>
            </div>
         </div>

         <!-- Address, Street, City, State -->
         <div class="row">
            <div class="col-md-6">
               <div class="form-group">
                  <input type="text" class="form-control" id="address" name="address" placeholder="Address" required>
               </div>
            </div>
            <div class="col-md-6">
               <div class="form-group">
                  <input type="text" class="form-control" id="street" name="street" placeholder="Stree" required>
               </div>
            </div>
         </div>

         <div class="row">
            <div class="col-md-6">
               <div class="form-group">
                  <input type="text" class="form-control" id="city" name="city" placeholder="City" required>
               </div>
            </div>
            <div class="col-md-6">
               <div class="form-group">
                  <input type="text" class="form-control" id="state" name="state" placeholder="State" required>
               </div>
            </div>
         </div>

         <!-- Email and Phone -->
         <div class="row">
            <div class="col-md-6">
               <div class="form-group">
                  <input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
               </div>
            </div>
            <div class="col-md-6">
               <div class="form-group">
                  <input type="tel" class="form-control" id="phone" name="phone" placeholder="Phone" required>
               </div>
            </div>
         </div>

         <!-- Submit button -->
         <button type="submit" id="submitBtn" class="btn btn-primary btn-sm">Add Customer</button>
      </form>
   </div>

</div>


<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<script>
   $(document).ready(function () {

      loadCustomerData(currentPage);

      // SUBMIT BUTTON CLICK
      $('#submitBtn').on('click', function () {
         if ($(this).text() === 'Update') {
            updateCustomer(customerId);
         } else {
            addCustomer();
         }
      });

      // UPDATE ICON CLICK
      $(document).on('click', '.fa-pen', function () {
         customerId = $(this).data('customer-id');
         $.ajax({
            type: 'GET',
            url: '/api/customer/customerById/' + customerId,
            headers: {
               'Authorization': authorizationToken
            },
            success: function (data) {
               if (data.success) {
                  var customer = data.responseObject;
                  $('#firstName').val(customer.firstName);
                  $('#lastName').val(customer.lastName);
                  $('#address').val(customer.address);
                  $('#street').val(customer.street);
                  $('#city').val(customer.city);
                  $('#state').val(customer.state);
                  $('#email').val(customer.email);
                  $('#phone').val(customer.phone);
                  $('#submitBtn').text('Update');
               }
            },
            error: function (error) {
               console.error('Error fetching customer data for edit:', error);
            }
         });
      });

   });

   var pageSize = 5;
   var currentPage = 1;
   var authorizationToken = '<%= response.getHeader("Authorization") %>';
   var customerId = null;

   // FOR LOADING CUSTOMER DATA
   function loadCustomerData(page) {
      $.ajax({
         type: 'GET',
         url: '/api/customer/getCustomers',
         data: {page: page - 1, size: pageSize},
         headers: {
            'Authorization': authorizationToken
         },
         success: function (data) {
            displayCustomerData(data.responseObject.content);
            renderPagination(data.responseObject.totalPages);
         },
         error: function () {
            console.error('Error fetching customer data');
         }
      });
   }

   // FOR SHOWING CUSTOMER IN TABLE
   function displayCustomerData(customers) {
      var tableBody = $('#customerTableBody');
      tableBody.empty();

      for (var i = 0; i < customers.length; i++) {
         var customer = customers[i];
         var row = '<tr>';
         row += '<td>' + customer.firstName + '</td>';
         row += '<td>' + customer.lastName + '</td>';
         row += '<td>' + customer.address + '</td>';
         row += '<td>' + customer.street + '</td>';
         row += '<td>' + customer.city + '</td>';
         row += '<td>' + customer.state + '</td>';
         row += '<td>' + customer.email + '</td>';
         row += '<td>' + customer.phone + '</td>';
         row += '<td>';
         row += '<i class="fa-solid fa-trash mr-3 update-customer-icon" style="cursor: pointer;" data-customer-id="' + customer.id + '" onclick="deleteCustomer(\'' + customer.id + '\')"></i>';
         row += '<i class="fa-solid fa-pen" style="cursor: pointer;"  data-customer-id="' + customer.id + '" ></i>';
         row += '</td>';
         row += '</tr>';
         tableBody.append(row);
      }
   }

   // FOR PAGINATION
   function renderPagination(totalPages) {
      var pagination = $('#pagination');
      pagination.empty();

      for (var i = 1; i <= totalPages; i++) {
         var liClass = (i === currentPage) ? 'page-item active' : 'page-item';
         var li = '<li class="' + liClass + '"><a class="page-link" href="#" onclick="changePage(' + i + ')">' + i + '</a></li>';
         pagination.append(li);
      }
   }

   // FOR CHANGING PAGE
   function changePage(page) {
      currentPage = page;
      loadCustomerData(currentPage);
   }

   // FOR DELETING CUSTOMER
   function deleteCustomer(customerId) {
      $.ajax({
         type: 'GET',
         url: '/api/customer/deleteCustomer/' + customerId,
         headers: {
            'Authorization': authorizationToken
         },
         success: function (data) {
            if (data.success) {
               loadCustomerData(currentPage);
            }
         },
         error: function () {
            console.error('Error deleting customer');
         }
      });
   }

   // FOR ADDING NEW CUSTOMER
   function addCustomer() {
      event.preventDefault();
      var customerData = {
         firstName: $('#firstName').val(),
         lastName: $('#lastName').val(),
         address: $('#address').val(),
         street: $('#street').val(),
         city: $('#city').val(),
         state: $('#state').val(),
         email: $('#email').val(),
         phone: $('#phone').val()
      };

      $.ajax({
         type: 'POST',
         url: '/api/customer/addCustomer',
         contentType: 'application/json',
         data: JSON.stringify(customerData),
         headers: {
            'Authorization': authorizationToken
         },
         success: function (data) {
            if (data.success) {
               $('#firstName').val(''),
                 $('#lastName').val(''),
                 $('#address').val(''),
                 $('#street').val(''),
                 $('#city').val(''),
                 $('#state').val(''),
                 $('#email').val(''),
                 $('#phone').val('')
               loadCustomerData(currentPage);
            }
         },
         error: function (error) {
            console.error('Error adding customer:', error);
         }
      });


   }

   // FOR UPDATING CUSTOMER
   function updateCustomer(customerId) {
      event.preventDefault();
      var customerData = {
         firstName: $('#firstName').val(),
         lastName: $('#lastName').val(),
         address: $('#address').val(),
         street: $('#street').val(),
         city: $('#city').val(),
         state: $('#state').val(),
         email: $('#email').val(),
         phone: $('#phone').val()
      };

      $.ajax({
         type: 'POST',
         url: '/api/customer/updateCustomer/' + customerId,
         contentType: 'application/json',
         data: JSON.stringify(customerData),
         headers: {
            'Authorization': authorizationToken
         },
         success: function (data) {
            if (data.success) {
               $('#firstName').val(''),
                 $('#lastName').val(''),
                 $('#address').val(''),
                 $('#street').val(''),
                 $('#city').val(''),
                 $('#state').val(''),
                 $('#email').val(''),
                 $('#phone').val('')
               loadCustomerData(currentPage);
               $('#submitBtn').text('Add');
            }
         },
         error: function (error) {
            console.error('Error adding customer:', error);
         }
      });


   }

   // FOR SEARCHING CUSTOME
   function searchCustomers() {
      var searchCriteria = $('#searchCriteria').val();
      var searchValue = $('#searchValue').val().trim();

      if (searchValue.trim() === "") {
         loadCustomerData(currentPage);
         return;
      }
      performSearch(searchCriteria, searchValue);
   }

   // FOR PERFORMING SEARCH
   function performSearch(criteria, value) {
      $.ajax({
         type: 'GET',
         url: '/api/customer/search',
         data: {
            criteria: criteria,
            value: value
         },
         headers: {
            'Authorization': authorizationToken
         },
         success: function (data) {
            if (data.success) {
               displayCustomerData(data.responseObject);
               if (data.responseObject.totalPages > 1) {
                  renderPagination(data.responseObject.totalPages);
               } else {
                  $('#pagination').empty();
               }
            }
         },
         error: function (error) {
            console.error('Error searching customers:', error);
         }
      });
   }

   function sync(){
      $.ajax({
         type: 'GET',
         url: '/api/customer/sync',
         headers: {
            'Authorization': authorizationToken
         },
         success: function (data) {
            if (data.success) {
               loadCustomerData(currentPage);
            }
         },
         error: function () {
            console.error('Sync Error');
         }
      });
   }

</script>

</body>
</html>
