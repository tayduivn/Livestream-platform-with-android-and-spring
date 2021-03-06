package com.t4.LiveServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    private Integer userId;
    private String wowzaId;
    private String userName;
    private String password;
    private String nickName;
	private String description;
    private String gmail;
    private String forgotToken;
    private Integer subscribeTotal;
	private String avatar;
	private String background;
	private Integer isActivated = 1;
	private Integer isPublisher = 1;
	
	private List<Notification> notifications;
    private List<StreamType> favouriteType;
    private List<FavoriteSaved> favouriteSaved;
    @JsonIgnoreProperties(value = {"streams", "subscribers", "favouriteType", "notifications", "favouriteSaved"})
    private List<User> subscribers;
    private List<PaySubscription> paySubscriptions;
    @JsonIgnoreProperties(value = {"owner", "comments"})
    private List<Stream> streams;
    private List<Ranking> rankings;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "wowza_id")
    @JsonIgnore
    public String getWowzaId() {
        return wowzaId;
    }

    public void setWowzaId(String wowzaId) {
        this.wowzaId = wowzaId;
    }

    @Column(name = "username")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password")
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "nick_name")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Column(name ="is_activated")
    public Integer getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Integer isActivated) {
        this.isActivated = isActivated;
    }

    @Column(name ="is_publisher")
    public Integer getIsPublisher() {
        return isPublisher;
    }

    public void setIsPublisher(Integer isPublisher) {
        this.isPublisher = isPublisher;
    }
	
	@Column(name ="description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
    @Column(name = "gmail")
    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    @Column(name = "forgot_token")
    @JsonIgnore
    public String getForgotToken() {
        return forgotToken;
    }

    public void setForgotToken(String forgotToken) {
        this.forgotToken = forgotToken;
    }

    @Column(name = "subscribe_total")
    public Integer getSubscribeTotal() {
        return subscribeTotal != null ? subscribeTotal : 0;
    }

    public void setSubscribeTotal(Integer subscribeTotal) {
        this.subscribeTotal = subscribeTotal;
    }
    
	@Column(name = "avatar")
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	@Column(name = "background")
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	
	
    @ManyToMany
    @JoinTable(name = "user_favourite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    public List<StreamType> getFavouriteType() {
        return favouriteType;
    }

    @ManyToMany
    @JoinTable(name = "notification_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "notify_id"))
    @JsonIgnore
    public List<Notification> getNotifications() {
        return notifications;
    }

    @OneToMany
    @JoinColumn(name = "user_id")
    public List<FavoriteSaved> getFavouriteSaved() {
        return favouriteSaved;
    }

    public void setFavouriteSaved(List<FavoriteSaved> favouriteSaved) {
        this.favouriteSaved = favouriteSaved;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setFavouriteType(List<StreamType> favouriteType) {
        this.favouriteType = favouriteType;
    }

    @ManyToMany
    @JoinTable(name = "subscriber",
            joinColumns = @JoinColumn(name = "publisher_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    @JsonIgnore
    public List<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public List<PaySubscription> getPaySubscriptions() {
        return paySubscriptions;
    }

    public void setPaySubscriptions(List<PaySubscription> paySubscriptions) {
        this.paySubscriptions = paySubscriptions;
    }

    @OneToMany
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public List<Ranking> getRankings() {
        return rankings;
    }

    public void setRankings(List<Ranking> rankings) {
        this.rankings = rankings;
    }

    public boolean checkExpiredSubscription() {
        if (paySubscriptions == null || paySubscriptions.size() == 0)
            return false;
        Date endTime = this.paySubscriptions.get(paySubscriptions.size()-1).getEndTime();
        if (endTime == null)
            return false;
        return (endTime.compareTo(new Date()) >= 0);
    }

    public void addPaySubscription(PaySubscription paySubscription) {
        paySubscriptions.add(paySubscription);
    }

    public Ranking getRankByYearAndMonth(int year, int month) {
        for (Ranking rank: rankings) {
            if (rank.getMonth() == month && rank.getYear() == year)
                return rank;
        }
        return null;
    }

    public void upRanking(int year, int month, int point) {
        Ranking ranking = getRankByYearAndMonth(year, month);
        if (ranking != null)
            ranking.setPoint(ranking.getPoint() + point);
        else {
            ranking = new Ranking();
            ranking.setYear(year);
            ranking.setMonth(month);
            ranking.setPoint(point);
            rankings.add(ranking);
        }
    }
}
