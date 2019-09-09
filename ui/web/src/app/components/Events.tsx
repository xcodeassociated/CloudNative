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
  reservations?: Array<Event>;
  error?: Error
}

class Events extends Component<object, IState> {

  constructor(props: object) {
    super(props);
    this.state = {
      reservations: undefined,
      error: undefined
    };
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
                reservations: objects
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

  public render() {
    if (this.hasToken() && this.state.error === undefined) {
      console.log(JSON.stringify(this.state.reservations));

      return (
        <div id="reservations-list">
          <MDBTable>
            <MDBTableHead>
              <tr className="reservations-list-table-header">
                <th className="reservations-list-table-header-id">ID</th>
                <th className="reservations-list-table-header-name">Name</th>
              </tr>
            </MDBTableHead>
            <MDBTableBody>
              {this.state.reservations !== undefined ?
                this.state.reservations
                  .map((reservation: Event, index: number) =>
                    <tr key={index} className="reservation-item">
                      <td>{reservation.eventId}</td>
                      <td>{reservation.eventName}</td>
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
