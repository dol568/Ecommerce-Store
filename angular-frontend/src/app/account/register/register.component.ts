import {Component, inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AccountService} from "../../core/_services/account.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PasswordValidators} from "../../shared/_helper/password-validators";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule],
    templateUrl: './register.component.html',
    styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit {
    #accountService = inject(AccountService);
    #activatedRoute = inject(ActivatedRoute);
    #router = inject(Router);
    signUpForm: FormGroup;
    returnUrl: string;
    readonly emailOnly = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

    constructor() {
        this.returnUrl = this.#activatedRoute.snapshot.queryParams['returnUrl'] ?? '/';
    }

    ngOnInit(): void {
        this.signUpForm = new FormGroup({
            username: new FormControl('', [Validators.required]),
            email: new FormControl('', [Validators.pattern(this.emailOnly), Validators.required]),
            password: new FormControl('',
                Validators.compose([
                    Validators.required,
                    Validators.minLength(8),
                    Validators.maxLength(30),
                    PasswordValidators.patternValidator(new RegExp("(?=.*[0-9])"), {
                        requiresDigit: true
                    }),
                    PasswordValidators.patternValidator(new RegExp("(?=.*[A-Z])"), {
                        requiresUppercase: true
                    }),
                    PasswordValidators.patternValidator(new RegExp("(?=.*[a-z])"), {
                        requiresLowercase: true
                    }),
                    PasswordValidators.patternValidator(new RegExp("(?=.*[$@^!%*?&])"), {
                        requiresSpecialChars: true
                    })
                ])
            )
        });
    }

    submitFunc() {
        this.#accountService.register(this.signUpForm.value).subscribe({
            next: () => this.#router.navigate([this.returnUrl]),
            error: err => console.error(err)
        });
    }

    get f() {
        return this.signUpForm.controls;
    }

    get passwordValid() {
        return this.signUpForm.controls["password"].errors === null;
    }

    get requiredValid() {
        return !this.signUpForm.controls["password"].hasError("required");
    }

    get minLengthValid() {
        return !this.signUpForm.controls["password"].hasError("minlength");
    }

    get requiresDigitValid() {
        return !this.signUpForm.controls["password"].hasError("requiresDigit");
    }

    get requiresUppercaseValid() {
        return !this.signUpForm.controls["password"].hasError("requiresUppercase");
    }

    get requiresLowercaseValid() {
        return !this.signUpForm.controls["password"].hasError("requiresLowercase");
    }

    get requiresSpecialCharsValid() {
        return !this.signUpForm.controls["password"].hasError("requiresSpecialChars");
    }
}
