import { Component, OnInit } from '@angular/core';
import { FeedbackEntry } from '../feedback-entry';
import { FeedbackService } from '../feedback.service';

@Component({
  selector: 'app-feedback-list',
  templateUrl: './feedback-list.component.html',
  styleUrls: ['./feedback-list.component.css']
})
export class FeedbackListComponent implements OnInit {

  feedbackEntries: FeedbackEntry[];
  contactType: string;
  sortOrder: string;

  constructor(private feedbackService: FeedbackService) {
    this.contactType = 'NONE';
    this.sortOrder = 'NONE';
  }

  onSubmit() {
    this.feedbackService.findAll(this.contactType, this.sortOrder).subscribe(data => {
      this.feedbackEntries = data;
    });
  }

  ngOnInit() {
    this.feedbackEntries = [];
  }
}
