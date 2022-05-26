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
                  @change="classNoChange"
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
          ref="jFileUpload"
          :disabled="false"
          businessType="training_briefing"
          :objectId="queryParam.classNo"
          text="上传培训简报"
          :key="uploadKey"
          :labelColumn="{ span: 0 }"
          :wrapperColumn="{ span: 24 }"
        />
      </div>

      <a-empty v-else description="请先选择一个培训班，再进行操作" />
    </a-card>
  </page-layout>
</template>

<script>
import Vue from 'vue'
import PageLayout from '@/components/page/PageLayout'

export default {
  name: 'TrainingBriefingList',
  components: {
    PageLayout,
  },
  data() {
    return {
      title: '培训简报',
      uploadKey: String(new Date()),
      queryParam: {},
    }
  },
  created() {
    this.init()
  },
  methods: {
    /**
     * 界面初始化时调用
     */
    init() {
      // 将用户习惯的查询取出
      Object.assign(this.queryParam, Vue.ls.get('USER_QUERY_CLASS_NO'))
    },
    /**
     * 培训班变更时调用
     */
    classNoChange() {
      this.uploadKey = String(new Date())
      // 将用户习惯的查询取出
      if (this.queryParam.classNo) Vue.ls.set('USER_QUERY_CLASS_NO', { classNo: this.queryParam.classNo })
    },
  },
}
</script>

<style lang="less" scoped>
</style>