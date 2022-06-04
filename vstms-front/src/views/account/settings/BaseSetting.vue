<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">
        <a-form layout="vertical">
          <a-form-item label="用户名">
            <a-input v-model="model.username" disabled />
          </a-form-item>
          <a-form-item label="姓名">
            <a-input placeholder="请输入姓名" v-model="model.realname" :disabled="!editing" />
          </a-form-item>
          <a-form-item label="生日">
            <a-date-picker
              style="width: 100%"
              placeholder="请选择生日"
              v-model="model.birthday"
              format="YYYY-MM-DD"
              :getCalendarContainer="(node) => node.parentNode"
              :disabled="!editing"
            />
          </a-form-item>
          <a-form-item label="性别">
            <a-select v-model="model.sex" placeholder="请选择性别" :disabled="!editing">
              <a-select-option :value="1">男</a-select-option>
              <a-select-option :value="2">女</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item>
            <a-button @click="handleSubmit" type="primary" v-if="editing" :loading="loading">提交</a-button>
            <a-button @click="handleCancel" v-if="editing" :loading="loading" style="margin-left: 8px">取消</a-button>
            <a-button @click="handleEdit" v-else>编辑</a-button>
          </a-form-item>
        </a-form>
      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <!-- <div class="ant-upload-preview" @click="handleAvatar">
          <a-icon type="cloud-upload-o" class="upload-icon" />
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="getFileAccessHttpUrl(model.avatar)" />
        </div> -->
        <!-- <a-form-item label="头像图片地址">
          <a-input placeholder="请输入头像图片地址" v-model="model.avatar" :disabled="!editing" />
        </a-form-item> -->
        <a-form-item label="头像">
          <j-image-upload
            class="avatar-uploader"
            text="上传"
            v-model="model.avatar"
            :disabled="!editing"
          ></j-image-upload>
        </a-form-item>
      </a-col>
    </a-row>

    <avatar-modal ref="modal"> </avatar-modal>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getAction, putAction, getFileAccessHttpUrl } from '@/api/manage'
import AvatarModal from './AvatarModal'

export default {
  components: {
    AvatarModal,
  },
  data() {
    return {
      // cropper
      preview: {},
      option: {
        img: '/avatar2.jpg',
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1],
      },
      loading: false,
      editing: false,
      model: {},
      url: {
        edit: '/sys/user/edit',
      },
    }
  },
  created() {
    this.model = this.userInfo()
    this.option.img = this.model.avatar
  },
  methods: {
    ...mapGetters(['userInfo']),
    getFileAccessHttpUrl(url) {
      return getFileAccessHttpUrl(url)
    },
    handleEdit() {
      this.editing = true
    },
    handleCancel() {
      this.editing = false
      this.model = this.userInfo()
    },
    handleSubmit() {
      this.loading = true
      // 提交表单
      const that = this
      putAction(that.url.edit, {
        id: that.model.id,
        realname: that.model.realname,
        birthday: that.model.birthday,
        sex: that.model.sex,
        avatar: that.model.avatar,
      })
        .then((res) => {
          if (res.success) {
            that.$message.success('保存成功')
          } else {
            that.$message.error(res.message)
          }
        })
        .finally(() => {
          that.loading = false
          that.editing = false
        })
    },
    handleAvatar() {
      this.$refs.modal.options = this.option
      this.$refs.modal.edit(1)
    },
  },
}
</script>

<style lang="less" scoped>
.avatar-upload-wrapper {
  height: 200px;
  width: 100%;
}

.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }
  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img,
  .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 50%;
    overflow: hidden;
  }
}
</style>