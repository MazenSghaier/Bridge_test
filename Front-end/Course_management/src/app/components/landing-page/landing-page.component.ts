import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgFor } from '@angular/common';


interface Course {
  title: string;
  price: string;
  image: string;
}

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [ReactiveFormsModule, NgFor],
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent {
  public courses: Course[] = [
    { title: 'Spring Boot / Angular', price: '350 DT/ Month', image: 'assets/images/spring-boot-angular.png' },
    { title: 'Node JS / React', price: '350 DT/ Month', image: 'assets/images/node-react.png' },
    { title: 'Flutter / Firebase', price: '350 DT/ Month', image: 'assets/images/flutter-firebase.png' },
    { title: 'Business Intelligence', price: '350 DT/ Month',  image: 'assets/images/business-intelligence.png' },
    { title: 'Artificial Intelligence', price: '350 DT/ Month', image: 'assets/images/artificial-intelligence.png' },
    { title: 'Devops', price: '350 DT/ Month', image: 'assets/images/devops.png' },
  ];

  contactForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.contactForm = this.fb.group({
      name: [''],
      email: [''],
      message: ['']
    });
  }

  onSubmit() {
    console.log(this.contactForm.value);
  }
}