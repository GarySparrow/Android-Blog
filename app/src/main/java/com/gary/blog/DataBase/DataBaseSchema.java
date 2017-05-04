package com.gary.blog.DataBase;

/**
 * Created by hasee on 2016/12/23.
 */

public class DataBaseSchema {

    public static final class PostTable {
        public static final String NAME = "post";

        public static final class Cols {
            public static final String postUrl = "post_url";
            public static final String authorUrl = "author_url";
            public static final String subject = "subject";
            public static final String timeStamp = "timestamp";
            public static final String id = "id";

        }
    }
}
