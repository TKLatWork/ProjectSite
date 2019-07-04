<template>
  <div id="NavHeader">
    <div>
      <!-- 导航 -->
      <div id="Navs">
        <!-- 主按钮 -->
        <span id="MainInfo">
          <el-button type="primary" icon="el-icon-menu"></el-button>
          <i>{{getDispalyName()}}</i>
        </span>
        <router-link to="/">Home</router-link> |
        <router-link to="/blog">Blog</router-link>
      </div>
    </div>
    <div id="SideMenu">
      <h1>SideMenu</h1>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { State, Action, Getter } from 'vuex-class';
import { Account } from '../model/account/Account';
import { AccountEnum } from '../model/account/AccountEnum';

@Component
export default class NavHeader extends Vue {
  @State private account!: Account;

  public getDispalyName() {
    if (!this.account || this.account.role === AccountEnum.ROLE_VISTOR) {
      return '游客';
    } else {
      return this.account.name;
    }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
$border-size: 2px;
$SideMenu-size: 180px;

#NavHeader{
  position: fixed;
  width: 100%;
  top: 0px;
  z-index: -1;
  bottom: 0px;
}

#Navs{
  height: 40px;
  width: 100%;
  border: $border-size solid black;
}

#MainInfo{
  display: inline-block;
  width: $SideMenu-size;
}

#SideMenu{
  width: $SideMenu-size;
  border: $border-size solid black;
  margin-top: -40 - 2 * $border-size;
  padding-top: 40px;
  height: 100%;
}

</style>
