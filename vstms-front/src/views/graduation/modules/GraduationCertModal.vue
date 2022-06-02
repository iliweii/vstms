<template>
  <j-modal
    title="培训证书查看/下载"
    :visible="visible"
    :confirm-loading="confirmLoading"
    :fullscreen="true"
    :okClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <template slot="footer">
      <a-button key="back" @click="handleCancel"> 取消 </a-button>
      <a-button key="submit" type="primary" @click="handleOk"> 确定 </a-button>
      <!-- 打印按钮 -->
      <a-button type="primary" v-print="'#cert-print'" icon="printer">打印</a-button>
    </template>
    <div class="cert-flex">
      <div class="cert-body" id="cert-print">
        <img src="@/assets/cert.png" alt="培训证书" class="cert-img" />
        <div class="cert-info">
          <div class="cert-info-title">
            <span class="cert-info-title-value">{{ model.no }}</span>
          </div>
          <div class="cert-info-title">
            <span class="cert-info-title-value">{{ model.studentName }}</span>
          </div>
          <div class="cert-info-title">
            <span class="cert-info-title-value">{{ model.skillName }}</span>
          </div>
          <div class="cert-info-title">
            <span class="cert-info-title-value">{{ model.studentId }}</span>
          </div>
          <div class="cert-info-title">
            <span class="cert-info-title-value">{{ new Date(model.createTime).getFullYear() }}</span>
          </div>
          <div class="cert-info-title">
            <span class="cert-info-title-value cert-margin-left">{{
              bl(new Date(model.createTime).getMonth() + 1)
            }}</span>
          </div>
          <div class="cert-info-title">
            <span class="cert-info-title-value cert-margin-left">{{ bl(new Date(model.createTime).getDate()) }}</span>
          </div>
        </div>
      </div>
    </div>
  </j-modal>
</template>
<script>
export default {
  name: 'GraduationCertModal',
  data() {
    return {
      ModalText: 'Content of the modal',
      visible: false,
      confirmLoading: false,

      model: {},
    }
  },
  methods: {
    showModal(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    handleOk(e) {
      this.ModalText = 'The modal will be closed after two seconds'
      this.confirmLoading = true
      setTimeout(() => {
        this.visible = false
        this.confirmLoading = false
      }, 2000)
    },
    handleCancel(e) {
      this.visible = false
    },
    bl(a) {
      return a < 10 ? '0' + a : a
    },
  },
}
</script>

<style lang="less">
.cert-flex {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.cert-body {
  position: relative;
  width: auto;
  height: auto;
}

.cert-info {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.cert-info-title:nth-child(1) {
  position: absolute;
  top: 16%;
  left: 22%;
}
.cert-info-title:nth-child(2) {
  position: absolute;
  top: 46%;
  left: 20%;
}
.cert-info-title:nth-child(3) {
  position: absolute;
  top: 53%;
  left: 34%;
}
.cert-info-title:nth-child(4) {
  position: absolute;
  top: 62.5%;
  left: 32%;
}

.cert-info-title:nth-child(5) {
  position: absolute;
  top: 74%;
  left: 59%;
}
.cert-info-title:nth-child(6) {
  position: absolute;
  top: 74%;
  left: 68.5%;
}
.cert-info-title:nth-child(7) {
  position: absolute;
  top: 74%;
  left: 76.5%;
}
.cert-info-title-value {
  font-family: '华文隶书';
  font-size: 18px !important;
  color: #030303;
}
</style>