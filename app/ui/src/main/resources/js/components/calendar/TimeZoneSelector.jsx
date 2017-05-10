import React from 'react';

import CalendarController from '../../controller/CalendarController';

export default class TimeZoneSelector extends React.Component {

    static propTypes = {
        value: React.PropTypes.string,
        onChange: React.PropTypes.func
    };

    areaOptions = [];

    constructor( props ) {
        super( props );
        var area = "";
        if ( props.value ) {
            var parts = props.value.split( "/" );
            if ( parts[0] ) {
                area = parts[0];
            }
        }
        this.state = {
            timezones: [],
            areas: [],
            area: area,
            timezone: props.value
        }
        this.changeArea = this.changeArea.bind( this );
        this.changeTimezone = this.changeTimezone.bind( this );
    }

    componentDidMount() {
        var component = this;
        CalendarController.getTimezones(
            function( timezones ) {
                var areas = [];
                for ( var timezone of timezones ) {
                    var id = timezone.id;
                    var parts = id.split( "/" );
                    if ( areas.indexOf( parts[0] ) < 0 ) {
                        areas.push( parts[0] );
                    }
                }
                areas.sort();
                for ( var area of areas ) {
                    component.areaOptions.push(
                        <option key={area} value={area}>{area}</option>
                    );
                }
                component.setState( { timezones: timezones, areas: areas })
            },
            function( response ) { }
        );
    }

    changeArea( event ) {
        var area = event.target.value;
        if ( this.props.onChange ) {
            this.props.onChange( '' );
        }
        this.setState( {
            area: area,
            timezone: ''
        });
    }

    changeTimezone( event ) {
        var timezone = event.target.value;
        if ( this.props.onChange ) {
            this.props.onChange( timezone );
        }
        this.setState( {
            timezone: timezone
        });
    }

    render() {
        var options = [];
        if ( this.state.area ) {
            for ( var timezone of this.state.timezones ) {
                if ( timezone.id.startsWith( this.state.area ) ) {
                    var subArea = timezone.id.substring( this.state.area.length + 1, timezone.id.length );
                    options.push(
                        <option key={timezone.id} value={timezone.id}>GMT{timezone.offset} h - {subArea} ({timezone.name} )</option>
                    );
                }
            }
        }
        return ( <div className="row">
            <div className="form-group col-md-6">
                <label htmlFor="area">Area</label>
                <select className="form-control" id="area" name="area" required="true" value={this.state.area} onChange={this.changeArea}>
                    {this.areaOptions}
                </select>
            </div>
            <div className="form-group col-md-6">
                <label htmlFor="timezone">Timezone</label>
                <select className="form-control" id="timezone" name="timezone" required="true" value={this.state.timezone} onChange={this.changeTimezone}>
                    {options}
                </select>
            </div></div>
        );
    }

}
