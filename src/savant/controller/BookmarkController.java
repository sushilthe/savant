/*
 *    Copyright 2010 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*
 * BookmarkController.java
 * Created on Jan 26, 2010
 */

/**
 * Controller object to manage changes to bookmarks.
 * @author mfiume
 */
package savant.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import savant.controller.event.bookmark.BookmarksChangedEvent;
import savant.controller.event.bookmark.BookmarksChangedListener;
import savant.util.Bookmark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookmarkController {

    private static BookmarkController instance;

    private static Log log = LogFactory.getLog(RangeController.class);

    private List<Bookmark> bookmarks;

    private List<BookmarksChangedListener> favoritesChangedListeners;

    public static BookmarkController getInstance() {
        if (instance == null) {
            instance = new BookmarkController();
        }
        return instance;
    }

    private BookmarkController() {
        favoritesChangedListeners = new ArrayList<BookmarksChangedListener>();
        bookmarks = new ArrayList<Bookmark>();
    }

    public void addBookmark(Bookmark f) {
        this.bookmarks.add(f);
        this.fireFavoritesChangedEvent();
    }

    public void removeBookmark() {
        this.removeBookmark(this.bookmarks.size()-1);
    }

    public void removeBookmark(int index) {
        try {
            this.bookmarks.remove(index);
            this.fireFavoritesChangedEvent();
        } catch(Exception e) {}
    }

    private List<Bookmark> getBookmarks() {
        return this.bookmarks;
    }

    /**
     * Fire the RangeChangedEvent
     */
    private synchronized void fireFavoritesChangedEvent() {
        BookmarksChangedEvent evt = new BookmarksChangedEvent(this, this.bookmarks);
        for (BookmarksChangedListener listener : this.favoritesChangedListeners) {
            listener.bookmarksChangeReceived(evt);
        }
    }

    public synchronized void addFavoritesChangedListener(BookmarksChangedListener l) {
        favoritesChangedListeners.add(l);
    }

    public synchronized void removeFavoritesChangedListener(BookmarksChangedListener l) {
        favoritesChangedListeners.remove(l);
    }

    public void addCurrentRangeToBookmarks() {
        RangeController rc = RangeController.getInstance();
        if (rc.getRange() != null) {
            this.addBookmark(new Bookmark(rc.getRange()));
        }
    }

    public Bookmark getFavorite(int index) {
        return this.bookmarks.get(index);
    }

}
