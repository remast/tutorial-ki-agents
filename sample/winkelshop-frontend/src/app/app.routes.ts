import { Routes } from '@angular/router';

import { guestOnlyGuard } from './auth/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [guestOnlyGuard],
    loadComponent: () =>
      import('./auth/login-page').then(
        (module) => module.LoginPage
      )
  },
  {
    path: '',
    loadComponent: () =>
      import('./winkelshop/winkelshop-page').then(
        (module) => module.WinkelshopPage
      )
  },
  {
    path: '**',
    redirectTo: ''
  }
];
