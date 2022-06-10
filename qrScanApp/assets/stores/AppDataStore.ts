import {action, makeObservable, observable} from 'mobx';
import AppData from '../../config/standard/data/AppData';

/**
 * APP 정보 저장소
 */
export class AppDataStore {

    constructor() {
        makeObservable(this);
    }

    /**
     * 로컬 정보
     */
    @observable
    appData = new AppData();

    /**
     * 로컬 정보 셋팅
     * @param locale 로컬 객체
     */
    @action
    setAppData = (appData: AppData) => {
        this.appData = appData;
    }
}