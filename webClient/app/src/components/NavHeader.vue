<template>
  <div id="NavHeader">
    <div>
      <!-- 导航 -->
      <div id="Navs">
        <!-- 主按钮 -->
        <el-button type="primary" icon="el-icon-menu"></el-button>
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

@Component
export default class NavHeader extends Vue {

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
$border-size: 2px;

#NavHeader{
  position: fixed;
  width: 100%;
  top: 0px;
  bottom: 0px;
}

#Navs{
  height: 40px;
  width: 100%;
  border: $border-size solid black;
}

#SideMenu{
  width: 180px;
  border: $border-size solid black;
  margin-top: -40 - 2 * $border-size;
  padding-top: 40px;
  height: 100%;
}

</style>
