import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from "@angular/router";
import { AuthenticationService } from '../../../_services';

@Component({
    selector: 'app-login-page',
    templateUrl: './login-page.component.html',
    styleUrls: ['./login-page.component.scss']
})

export class LoginPageComponent {

    @ViewChild('f') loginForm: NgForm;
    returnUrl: string;

    constructor(private router: Router,
        private route: ActivatedRoute,
        private authenticationService: AuthenticationService) { }

    // On submit button click    
    onSubmit() {
        console.log(this.loginForm);
        console.log(this.loginForm.controls.inputEmail);
        this.authenticationService.login(this.loginForm.controls.inputEmail.value, this.loginForm.controls.inputPass.value)
        .subscribe(
            data => {
                this.router.navigate([this.returnUrl]);
            },
            error => {
                // TODO
            });;
        this.loginForm.reset();
    }
    // On Forgot password link click
    onForgotPassword() {
        this.router.navigate(['forgotpassword'], { relativeTo: this.route.parent });
    }
    // On registration link click
    onRegister() {
        this.router.navigate(['register'], { relativeTo: this.route.parent });
    }

    ngOnInit() {
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
    }
}