<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
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
                  @change="
                    () => {
                      queryParam.no = `${queryParam.classNo}_testimonials_${userInfo().username}`
                      queryParam.type = '1'
                      loadData()
                    }
                  "
                />
              </a-form-item>
            </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>

                <!-- 操作按钮 -->
                <a-button
                  type="primary"
                  icon="edit"
                  style="margin-left: 40px"
                  v-if="!editing && queryParam.classNo"
                  @click="handleEdit"
                  >点击编辑</a-button
                >
                <a-button
                  type="primary"
                  icon="check"
                  style="margin-left: 40px"
                  v-if="editing && queryParam.classNo"
                  @click="handleOk"
                  >完成并保存</a-button
                >
                <a-button
                  type="primary"
                  icon="close"
                  style="margin-left: 8px"
                  v-if="editing && queryParam.classNo"
                  @click="handleCancel"
                  >取消编辑</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 内容区域 -->
      <div v-if="queryParam.classNo">
        <!-- 隐藏的Table -->
        <a-table
          v-show="false"
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          class="j-table-force-nowrap"
        >
        </a-table>

        <!-- 表单区域 -->
        <a-form-model ref="form" :model="model" :rules="validatorRules">
          <a-form-model-item :labelCol="{ span: 0 }" :wrapperCol="{ span: 24 }" prop="content" label="内容">
            <j-editor v-model="model.content" autoSize :style="{ height: '500px' }" :disabled="!editing" />
          </a-form-model-item>
        </a-form-model>
      </div>

      <a-empty v-else description="请先选择一个培训班，再进行操作" />
      <!-- 内容区域-end -->

      <!-- 表单区域 -->
      <trainingSummary-modal ref="modalForm" @ok="modalFormOk"></trainingSummary-modal>
    </a-card>
  </page-layout>
</template>

<script>
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import TrainingSummaryModal from './modules/TrainingSummaryModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, httpAction } from '@/api/manage'
import { mapGetters } from 'vuex'

// 编号逻辑，总结类型包括：
// 学员感言      [classNo+'testimonials'+username]
// 学员培训总结   [classNo+'student'+username]
// 教师自评报告   [classNo+'teacher'+username]
// 培训典型案例   [classNo+'typicalCase']
// 督导总结      [classNo+'supervision']
// 培训班总结     [classNo]
export default {
  name: 'SummaryTestimonials',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    TrainingSummaryModal,
  },
  data() {
    return {
      description: '培训总结表管理页面',
      title: '学员感言',

      editing: false,
      model: {},
      validatorRules: {},
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '总结编号',
          align: 'center',
          dataIndex: 'no',
        },
        {
          title: '总结内容',
          align: 'center',
          dataIndex: 'content',
        },
      ],
      url: {
        list: '/training/trainingSummary/list',
        add: '/training/trainingSummary/add',
        edit: '/training/trainingSummary/edit',
      },
    }
  },
  methods: {
    ...mapGetters(['userInfo']),
    /**
     * 加载数据
     */
    loadData() {
      var params = this.getQueryParams() //查询条件
      this.loading = true
      getAction(this.url.list, params)
        .then((res) => {
          if (res.success) {
            this.dataSource = res.result.records || res.result
            if (res.result.total) {
              this.ipagination.total = res.result.total
              // 有数据，第一条即为有效数据，取第一条
              this.model = this.dataSource[0]
            } else {
              this.ipagination.total = 0
              // 无数据，初始化数据
              this.model = Object.assign({}, this.queryParam)
            }
          } else {
            this.$message.warning(res.message)
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    /**
     * 编辑
     */
    handleEdit() {
      this.editing = true
    },
    /**
     * 提交表单
     */
    handleOk() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')

                that.model = res.result
                // 取消编辑
                that.editing = false
                // 加载数据
                that.loadData()
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
              that.close()
            })
        } else {
          return false
        }
      })
    },
    handleCancel() {
      this.editing = false
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>