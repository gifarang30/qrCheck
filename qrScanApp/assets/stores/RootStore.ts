import {AppDataStore} from '../stores/AppDataStore';

/**
 * 신규 스토어 생성시 추가 필요
 */
export interface StoreType {
    appDataStore: AppDataStore
}
const stores: StoreType = {
    appDataStore: new AppDataStore()
}

export default function RootStore() {
    return stores;
}