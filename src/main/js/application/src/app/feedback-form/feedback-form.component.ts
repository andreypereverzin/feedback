import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FeedbackEntry } from '../feedback-entry';
import { FeedbackService } from '../feedback.service';

@Component({
  selector: 'app-feedback-form',
  templateUrl: './feedback-form.component.html',
  styleUrls: ['./feedback-form.component.css']
})
export class FeedbackFormComponent {

  feedbackEntry: FeedbackEntry;

  constructor(
    private route: ActivatedRoute,
      private router: Router,
        private feedbackService: FeedbackService) {
    this.feedbackEntry = new FeedbackEntry();
    this.feedbackEntry.contactType = 'GENERAL';
  }

  onSubmit() {
    this.feedbackService.save(this.feedbackEntry).subscribe(result => this.gotoUser());
  }

  gotoUser() {
    window.alert('Feedback submitted successfully');

    this.feedbackEntry = new FeedbackEntry();
    this.feedbackEntry.contactType = 'GENERAL';
    this.router.navigate(['/user']);
  }
}
