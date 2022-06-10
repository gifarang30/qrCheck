import {ChangeEvent, ReactNode, SyntheticEvent} from 'react';

/**
 * json 타입
 */
export interface JsonType {
    [key: string]: string | number | boolean | any[] | JsonType | undefined
}

/**
 * 폼 이벤트 타입
 */
export interface FormEventType {
    name?: string | number | symbol | any,
    value: unknown,
    type: string,
    checked: unknown
}

/**
 * Change 이벤트 타입
 */
export type ChangeEventType = ChangeEvent<HTMLTextAreaElement | HTMLInputElement | FormEventType | { name?: string; value: unknown; type?: string; event: Event | SyntheticEvent }>

export type ChangeChildType = ReactNode | undefined