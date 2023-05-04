package NaslovnaStranica;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Vasiot kod ovde

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}

class Category {
    private final String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

abstract class NewsItem {
    protected final String title;
    protected final Date publishDate;
    protected Category category;

    public NewsItem(String title, Date publishDate, Category category) {
        this.title = title;
        this.publishDate = publishDate;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public abstract String getTeaser();

    @Override
    public String toString() {
        return getTeaser();
    }
}

class TextNewsItem extends NewsItem {
    private final String text;

    public TextNewsItem(String title, Date publishDate, Category category, String text) {
        super(title, publishDate, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        long duration = Calendar.getInstance().getTime().getTime() - publishDate.getTime();

        return String.format("%s%n%d%n%.80s%n", title, TimeUnit.MILLISECONDS.toMinutes(duration), text);
    }
}

class MediaNewsItem extends NewsItem {
    private final String url;
    private final int viewsCount;

    public MediaNewsItem(String title, Date publishDate, Category category, String url, int viewsCount) {
        super(title, publishDate, category);
        this.url = url;
        this.viewsCount = viewsCount;
    }

    @Override
    public String getTeaser() {
        long duration = Calendar.getInstance().getTime().getTime() - publishDate.getTime();

        return String.format("%s%n%d%n%s%n%d%n", title, TimeUnit.MILLISECONDS.toMinutes(duration), url, viewsCount);
    }
}

class FrontPage {
    private final List<NewsItem> news;
    private final Category[] categories;

    public FrontPage(Category[] categories) {
        this.news = new ArrayList<NewsItem>();
        this.categories = categories;
    }

    public void addNewsItem(NewsItem newsItem) {
        news.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        return this.news.stream().filter(i -> i.getCategory().getName().equals(category.getName())).collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        List<NewsItem> result = this.news.stream().filter(i -> i.getCategory().getName().equals(category)).collect(Collectors.toList());

        for (Category c : categories)
            if (c.getName().equals(category))
                return result;

        throw new CategoryNotFoundException(String.format("Category %s was not found", category));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        news.forEach(sb::append);

        return sb.toString();
    }
}