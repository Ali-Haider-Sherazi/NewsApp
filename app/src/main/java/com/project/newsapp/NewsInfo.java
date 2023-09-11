package com.project.newsapp;

public class NewsInfo {

        private String title;
        private String type;
        private String date;
        private String preview;

        public NewsInfo()
        {

        }

        public NewsInfo(String title, String type, String date,String preview)
        {
            this.title = title;
            this.type = type;
            this.date = date;
            this.preview = preview;

        }

        public String getPreview()
        {
            return preview;
        }


        public String getName() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getDate() {
            return date;
        }





}
