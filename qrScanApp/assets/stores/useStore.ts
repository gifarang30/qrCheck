import React from 'react';
import {MobXProviderContext} from 'mobx-react';
import {StoreType} from './RootStore';

/**
 * React hooks 사용 컴포넌트에서 저장소를 가져올 때 사용
 */
const useStore = (): StoreType => {
    return <StoreType>React.useContext(MobXProviderContext);
}

export default useStore;