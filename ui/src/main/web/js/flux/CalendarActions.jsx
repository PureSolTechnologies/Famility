export const YEAR_CHANGE_ACTION = 'YEAR_CHANGE'
export const MONTH_CHANGE_ACTION = 'MONTH_CHANGE'
export const DAY_CHANGE_ACTION = 'DAY_CHANGE'

export function changeYear( year ) {
    return {
        type: YEAR_CHANGE_ACTION,
        year: year,
    };
}

export function changeMonth( month ) {
    return {
        type: MONTH_CHANGE_ACTION,
        month: month
    };
}

export function changeDay( day ) {
    return {
        type: DAY_CHANGE_ACTION,
        day: day
    };
}
