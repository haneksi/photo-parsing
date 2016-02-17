package ru.photoparser.parse.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.photoparser.entity.Album;
import ru.photoparser.entity.Image;
import ru.photoparser.entity.Portfolio;
import ru.photoparser.parse.Parser;
import ru.photoparser.util.ParserManagement;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/*
*  For site:  http://www.edpeers.com
*/
@Service("edpeersParser")
@Scope("singleton")
public class EdpeersParserImpl implements Parser {
    private final String URL = "http://www.edpeers.com";
    private final Document document = ParserManagement.getDocument(URL);
    private final String author = document.title();


    @Qualifier("portfolio")
    @Autowired
    private Portfolio portfolio;


    public EdpeersParserImpl() {
    }


    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getURL() {
        return URL;
    }

    @Override
    public Portfolio parsing(){
        this.portfolio.setUrl(URL);
        this.portfolio.setAuthor(author);
        List<Album> albums = new ArrayList<Album>();
        Elements links = document.select("span.read-more-wrap").select("a[href]");
        for (Element el: links ){
            String url = el.attr("href");
            String title = el.attr("title");
            Album album = new Album();
            album.setPortfolio(portfolio);
            album.setAuthor(author);
            album.setTitle(title);
            album.setUrl(url);
            List<Image> images = ParserManagement.getImagesToAlbumWP(album, portfolio, author);
            album.setImages(images);
            albums.add(album);
        }
        portfolio.setAlbums(albums);
        return portfolio;
    }

}