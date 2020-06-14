import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FeedbackEntry } from './feedback-entry';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class FeedbackService {

  private feedbackentriesUrl: string;

  constructor(private http: HttpClient) {
    this.feedbackentriesUrl = 'http://localhost:8080/feedback/feedbackentries';
  }

  public findAll(contactType, sortOrder): Observable<FeedbackEntry[]> {
    var path = this.feedbackentriesUrl;
    if (contactType != 'NONE' || sortOrder != 'NONE') {
      if (contactType != 'NONE') {
        path = path + '?contactType=' + contactType;
        if (sortOrder != 'NONE') {
          path = path + '&sortOrder=' + sortOrder;
        }
      } else {
        if (sortOrder != 'NONE') {
          path = path + '?sortOrder=' + sortOrder;
        }
      }
    }
    return this.http.get<FeedbackEntry[]>(path);
  }

  public save(feedbackentry: FeedbackEntry) {
    return this.http.post<FeedbackEntry>(this.feedbackentriesUrl, feedbackentry);
  }
}
