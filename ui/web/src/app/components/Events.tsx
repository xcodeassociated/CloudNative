import React, {Component} from 'react';
import {appConfig} from '../config/appConfig'
import '../style/Reservations.css'
import {MDBTable, MDBTableBody, MDBTableHead} from 'mdbreact';
import {Event} from "../model/Event";

type Error = {
  code: number,
  description: string
}

interface IState {
  events?: Array<Event>;
  error?: Error,
  eventCreateName: string,
  eventDeleteId: string,
  dispatch: Function
}

class Events extends Component<object, IState> {

  constructor(props: object) {
    super(props);
    this.state = {
      events: undefined,
      error: undefined,
      eventCreateName: "",
      eventDeleteId: "",
      dispatch: () => {}
    };

    this.onCreateSubmit = this.onCreateSubmit.bind(this);
    this.onDeleteSubmit = this.onDeleteSubmit.bind(this);
  }

  public hasToken(): boolean {
    return localStorage.getItem('token') != null;
  }

  public componentDidMount(): void {
    const token = localStorage.getItem('token');
    if (this.hasToken()) {
      const requestOptions = {
        method: 'GET',
        headers: [
          ['Content-Type', 'application/json'],
          ['Accept', 'application/json'],
          ['Authorization', `Bearer ${token}`]
        ],
      };

      const onFetch = (response: Response) => {
        if (response.ok) {
          response.text().then(text => {
              let objects: Array<Event> = JSON.parse(text);
              this.setState({
                ...this.state,
                events: objects
              })
            }
          )
        } else {
          response.text().then(text => {
            this.setState({
              ...this.state,
              error: {
                code: response.status,
                description: text
              }
            });
          });
        }
      };

      fetch(`${appConfig.backend_url}/api/v1/gateway/resource/events`, requestOptions).then(onFetch)
    }
  }

  private createEvent(eventName: string) {
    console.log("event create: " + eventName);

  }

  public onCreateSubmit(e: any) {
    e.preventDefault();
    this.state.dispatch(this.createEvent(this.state.eventCreateName));
  }

  private deleteEvent(eventId: string) {
    console.log("event delete: " + eventId);

  }

  public onDeleteSubmit(e: any) {
    e.preventDefault();
    let id = e.target.eventId.value;
    this.setState({eventDeleteId: id});
    this.state.dispatch(this.deleteEvent(id));
  }

  public render() {
    if (this.hasToken() && this.state.error === undefined) {
      console.log(JSON.stringify(this.state.events));
      let {eventCreateName} = this.state;
      return (
        <div id="reservations-list">
          <div id="event-create-form">
            <MDBTable>
              <MDBTableBody>
                <tr>
                  <td className="reservations-list-table-header-create-event">
                    <form name="eventCreateForm" onSubmit={this.onCreateSubmit}>
                      <div className="form-group-collection">
                        <div className="form-group">
                          <label>Event Name:</label>
                          <input type="text" className="form-control" name="email"
                                 onChange={e => this.setState({eventCreateName: e.target.value})} value={eventCreateName}/>
                        </div>
                      </div>
                      <div className="form-group">
                        <button className="btn btn-primary">Create Event</button>
                      </div>
                    </form>
                  </td>
                </tr>
              </MDBTableBody>
            </MDBTable>
          </div>
          <MDBTable>
            <MDBTableHead>
              <tr className="reservations-list-table-header">
                <th className="reservations-list-table-header-id">ID</th>
                <th className="reservations-list-table-header-name">Name</th>
              </tr>
            </MDBTableHead>
            <MDBTableBody>
              {this.state.events !== undefined ?
                this.state.events
                  .map((reservation: Event, index: number) =>
                    <tr key={index} className="reservation-item">
                      <td>{reservation.eventId}</td>
                      <td>{reservation.eventName}</td>
                      <td>
                        <form name="form" onSubmit={this.onDeleteSubmit}>
                          <input type="hidden" name="eventId" value={reservation.eventId}/>
                          <button className="btn btn-primary">Delete Event</button>
                        </form>
                      </td>
                    </tr>)
                : null
              }
            </MDBTableBody>
          </MDBTable>
        </div>
      );
    } else {
      return (
        <div id="reservations-error">
          <div id="reservations-error-code">
            <h2>
              {this.state.error !== undefined ?
                this.state.error.code
                : null}
            </h2>
          </div>
          <div id="reservations-error-description">
            <p>
              {this.state.error !== undefined ?
                this.state.error.description
                : null}
            </p>
          </div>
        </div>
      )
    }
  }
}

export default Events;