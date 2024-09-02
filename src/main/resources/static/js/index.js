document.addEventListener('DOMContentLoaded', function () {
               const mobileScreen = window.matchMedia("(max-width: 990px )");
               const menuToggles = document.querySelectorAll('.menu-toggle');
               const dashboardNav = document.querySelector('.dashboard-nav');
               const dashboard = document.querySelector('.dashboard');

               menuToggles.forEach(menuToggle => {
                   menuToggle.addEventListener('click', function () {
                       if (mobileScreen.matches) {
                           dashboardNav.classList.toggle('mobile-show');
                       } else {
                           dashboard.classList.toggle('dashboard-compact');
                       }
                   });
               });

               const dropdownToggles = document.querySelectorAll('.dashboard-nav-dropdown-toggle');

               dropdownToggles.forEach(dropdownToggle => {
                   dropdownToggle.addEventListener('click', function () {
                       const parentDropdown = this.closest('.dashboard-nav-dropdown');
                       parentDropdown.classList.toggle('show');
                       parentDropdown.siblings?.forEach(sibling => sibling.classList.remove('show'));
                   });
               });
           });