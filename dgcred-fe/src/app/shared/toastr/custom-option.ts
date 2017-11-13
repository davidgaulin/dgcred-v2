import {ToastOptions} from 'ng2-toastr';

//Toastr global configuration option
export class CustomOption extends ToastOptions {
  animate = 'flyRight'; // you can pass any options to override defaults
  dismiss = 'auto';
  toastLife = 3000;
}

