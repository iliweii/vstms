<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="培训班" required>
                <j-search-select-tag
                  ref="selectTag"
                  v-model="queryParam.classNo"
                  :triggerChange="true"
                  placeholder="请选择培训班"
                  dict="training_class,name,no"
                  :disabled="false"
                  @change="handleChange"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" icon="search">查询</a-button>
                <a-button type="primary" @click="() => (queryParam = {})" icon="reload" style="margin-left: 8px"
                  >重置</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 内容区域 -->
      <div v-if="queryParam.classNo">
        <j-file-upload
          v-show="false"
          ref="jFileUpload"
          :disabled="false"
          businessType="training_process_image"
          :objectId="queryParam.classNo"
          text="上传培训图片"
          fileType="image"
          :labelColumn="{ span: 0 }"
          :wrapperColumn="{ span: 24 }"
        />
        <!-- 操作区域 -->
        <div class="table-operator" v-if="upload.dataSource">
          <a-button @click="() => this.$refs.jFileUpload.open()" type="primary">上传培训图片</a-button>

          <a-button-group :style="{ marginLeft: '50px' }">
            <a-button @click="handlePre" :disabled="upload.ipagination.current == 1">
              <a-icon type="left" />上一页
            </a-button>
            <a-button>当前第{{ upload.ipagination.current }}页 </a-button>
            <a-button
              @click="handleNext"
              :disabled="
                upload.ipagination.current == Math.ceil(upload.ipagination.total / upload.ipagination.pageSize)
              "
            >
              下一页<a-icon type="right" />
            </a-button>
          </a-button-group>
        </div>
        <!-- 图片展览区 -->
        <a-card title="培训图片" v-if="upload.dataSource">
          <a-card-grid style="width: 25%; text-align: center" v-for="img in upload.dataSource" :key="img.id">
            <img :src="getFilePath(img.filePath)" :alt="img.fileAlias" :style="{ width: '100%' }" />
            <span class="ant-form-extra" :style="{ fontSize: '12px' }"> {{ img.fileAlias }} </span>
          </a-card-grid>
        </a-card>
      </div>

      <a-empty v-else description="请先选择一个培训班，再进行操作" />
    </a-card>
  </page-layout>
</template>

<script>
import PageLayout from '@/components/page/PageLayout'

export default {
  name: 'ProcessImageList',
  components: {
    PageLayout,
  },
  data() {
    return {
      title: '培训图片',
      queryParam: {},

      upload: {},
    }
  },
  methods: {
    handleChange() {
      setTimeout(() => {
        this.$nextTick(() => {
          this.upload = this.$refs.jFileUpload
        })
      }, 200)
    },
    getFilePath(path) {
      return window._CONFIG['staticDomainURL'] + '/' + path
    },
    handlePre() {
      let page = this.upload.ipagination.current - 1
      if (page < 0) page = 0
      this.$refs.jFileUpload.loadData(page)
      this.handleChange()
    },
    handleNext() {
      let page = this.upload.ipagination.current + 1
      if (page > this.upload.ipagination.total) page = this.upload.ipagination.total
      this.$refs.jFileUpload.loadData(page)
      this.handleChange()
    },
  },
}
</script>

<style lang="less" scoped>
/deep/ .ant-card-contain-grid > .ant-card-body {
  display: flex;
  flex-wrap: wrap;
}
</style>