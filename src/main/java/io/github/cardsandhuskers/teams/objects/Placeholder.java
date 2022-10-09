package io.github.cardsandhuskers.teams.objects;

import io.github.cardsandhuskers.teams.Teams;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static io.github.cardsandhuskers.teams.Teams.*;

public class Placeholder extends PlaceholderExpansion {
    private final Teams plugin;
    ArrayList<Team> teamArrayList;

    public Placeholder(Teams plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getIdentifier() {
        return "Teams";
    }
    @Override
    public String getAuthor() {
        return "cardsandhuskers";
    }
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public String onRequest(OfflinePlayer p, String s) {

        if(s.equalsIgnoreCase("teamPoints")) {
            if(handler.getPlayerTeam((Player) p)!= null) {
                return "" + handler.getTeamPoints(handler.getPlayerTeam((Player) p));
            } else {
                return "";
            }
        }
        if(s.equalsIgnoreCase("team")) {
            if(handler.getPlayerTeam((Player) p) != null) {
                return handler.getPlayerTeam((Player)p).getConfigColor() + handler.getPlayerTeam((Player)p).getTeamName();
            } else {
                return "No Team";
            }

        }
        if(s.equalsIgnoreCase("color")) {
            if(handler.getPlayerTeam((Player) p) != null) {
                return handler.getPlayerTeam((Player)p).getConfigColor();
            } else {
                return "&f";
            }
        }

        if(s.equalsIgnoreCase("position")) {
            ArrayList<TempPointsHolder> playerPoints = new ArrayList<>();
            for(Team t: handler.getTeams()) {
                for(OfflinePlayer player: t.getPlayers()) {
                    if(player != null) {
                        playerPoints.add(t.getPlayerTempPoints(player));
                    }
                }
            }
            Collections.sort(playerPoints, Comparator.comparing(TempPointsHolder:: getPoints));
            Collections.reverse(playerPoints);
            int i = 0;
            for(TempPointsHolder h:playerPoints) {
                if(h.getPlayer().equals(p)) {
                    i = playerPoints.indexOf(h) + 1;
                }
            }

            if(i == 1) {
                return i + "st";
            } else if(i == 2) {
                return i + "nd";
            } else if(i == 3){
                return i + "rd";
            } else {
                return i + "th";
            }
        }

        //Get lines, so check
        // if ahead != 1st place use 4 lines, 1st, ahead, team, behind
        //if it is equal, just use 3 lines, ahead, team, behind
        //if player is on 1st place team, then team, behind, behind
        //if player is on last place team, first, ahead, ahead, team

        teamArrayList = handler.getTempPointsSortedList();
        int pos = getPosition(handler.getPlayerTeam((Player)p));
        Team playerTeam = handler.getPlayerTeam((Player) p);
        int numTeams = teamArrayList.size();
        //if team 1st, line equals behind
        //if team 2nd, line equals team
        //if team 3-n-1 line equals ahead
        //if team n line equals ahead.ahead

        //always has first place
        if(s.equalsIgnoreCase("teamLine1")) {
            Team firstTeam = getFirstPlace();
            if(firstTeam != null) {
                if(playerTeam != null) {
                    if (playerTeam.equals(firstTeam)) {
                        return getPosition(firstTeam) + ". " + firstTeam.getConfigColor() + "&l" + firstTeam.getTeamName() + " &r(you)" + "    " + firstTeam.getTempPoints();
                    } else {
                        return getPosition(firstTeam) + ". " + firstTeam.getConfigColor() + "&l" + firstTeam.getTeamName() + "    &r" + firstTeam.getTempPoints();
                    }
                } else {
                    return getPosition(firstTeam) + ". " + firstTeam.getConfigColor() + "&l" + firstTeam.getTeamName() + "    &r" + firstTeam.getTempPoints();
                }
            } else {
                return "TEAM";
            }
        }

        if(s.equalsIgnoreCase("teamLine2")) {
            if(playerTeam == null) {
                return "TEAM";
            }
            if(pos == 1) {
                //first place
                Team behind = getBehind(playerTeam);
                if(behind != null) {
                    return getPosition(behind) + ". " + behind.getConfigColor() + "&l" + behind.getTeamName() + "    &r" + behind.getTempPoints();
                    //return String.format("%-30s", "T", "5000");
                } else {
                    return "TEAM";
                }
            } else if(pos == 2) {
                //second place
                if(playerTeam != null) {
                    return getPosition(playerTeam) + ". " + playerTeam.getConfigColor() + "&l" + playerTeam.getTeamName() + " &r(you)" + "    " + playerTeam.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == numTeams) {
                //last place
                Team ahead = getAhead(playerTeam);
                Team doubleAhead;
                if(ahead != null) {
                    doubleAhead = getAhead(ahead);
                } else {
                    return "TEAM";
                }

                if(doubleAhead != null) {
                    return getPosition(doubleAhead) + ". " + doubleAhead.getConfigColor() + "&l" + doubleAhead.getTeamName() + "     &r" + doubleAhead.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else {
                //everywhere else
                Team ahead = getAhead(playerTeam);
                if(ahead != null) {
                    return getPosition(ahead) + ". " + ahead.getConfigColor() + "&l" + ahead.getTeamName() + "   &r" + ahead.getTempPoints();
                } else {
                    return "TEAM";
                }
            }
        }
        if(s.equalsIgnoreCase("teamLine3")) {
            if(playerTeam == null) {
                return "TEAM";
            }
            if(pos == 1) {
                //first place
                Team behind = getBehind(playerTeam);
                Team doubleBehind;
                if(behind != null) {
                    doubleBehind = getBehind(getBehind(playerTeam));
                } else {
                    return "TEAM";
                }
                if(doubleBehind != null) {
                    return getPosition(doubleBehind) + ". " + doubleBehind.getConfigColor() + "&l" + doubleBehind.getTeamName() + "    &r" + doubleBehind.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == 2) {
                //second place
                Team behind = getBehind(playerTeam);
                if(behind != null) {
                    return getPosition(behind) + ". " + behind.getConfigColor() + "&l" + behind.getTeamName() + "    &r" + behind.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == numTeams) {
                //last place
                Team ahead = getAhead(playerTeam);
                if(ahead != null) {
                    return getPosition(ahead) + ". " + ahead.getConfigColor() + "&l" + ahead.getTeamName() + "     &r" + ahead.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else {
                //everywhere else
                if(playerTeam != null) {
                    return getPosition(playerTeam) + ". " + playerTeam.getConfigColor() + "&l" + playerTeam.getTeamName() + " &r(you)" + "   " + playerTeam.getTempPoints();
                } else {
                    return "TEAM";
                }
            }
        }
        if(s.equalsIgnoreCase("teamLine4")) {
            if(playerTeam == null) {
                return "TEAM";
            }
            if(pos == 1) {
                //first place
                Team behind = getBehind(playerTeam);
                Team doubleBehind;
                Team tripleBehind;
                if(behind != null) {
                    doubleBehind = getBehind(behind);
                    if(doubleBehind!= null) {
                        tripleBehind = getBehind(doubleBehind);
                    } else {
                        return "TEAM";
                    }
                } else {
                    return "TEAM";
                }
                if(tripleBehind != null) {
                    return getPosition(tripleBehind) + ". " + tripleBehind.getConfigColor() + "&l" + tripleBehind.getTeamName() + "    &r" + tripleBehind.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == 2) {
                //second place
                Team behind = getBehind(playerTeam);
                Team doubleBehind;
                if(behind != null) {
                    doubleBehind = getBehind(behind);
                } else {
                    return "TEAM";
                }

                if(doubleBehind != null) {
                    return getPosition(doubleBehind) + ". " + doubleBehind.getConfigColor() + "&l" + doubleBehind.getTeamName() + "    &r" + doubleBehind.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else if(pos == numTeams) {
                //last place
                if(playerTeam != null) {
                    return getPosition(playerTeam) + ". " + playerTeam.getConfigColor() + "&l" + playerTeam.getTeamName() + " &r(you)" + "     " + playerTeam.getTempPoints();
                } else {
                    return "TEAM";
                }
            } else {
                //everywhere else
                Team behind = getBehind(playerTeam);
                if(behind != null) {
                    return getPosition(behind) + ". " + behind.getConfigColor() + "&l" + behind.getTeamName() + "   &r" + behind.getTempPoints();
                } else {
                    return "TEAM";
                }
            }
        }

        return null;
    }


    public Team getAhead(Team t) {
        int teamIndex = teamArrayList.indexOf(t);
        if(teamIndex > 0) {
            return teamArrayList.get(teamIndex - 1);
        } else {
            return null;
        }
    }

    public Team getBehind(Team t) {
        int teamIndex = teamArrayList.indexOf(t);
        if(teamArrayList.size() > 0) {
            if(teamIndex < teamArrayList.size() - 1) {
                return teamArrayList.get(teamIndex + 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public Team getFirstPlace() {
        if(teamArrayList.size()>0) {
            Team team = teamArrayList.get(0);
            return team;
        } else {
            return null;
        }

    }
    public int getPosition(Team t) {
        try {
            int index = teamArrayList.indexOf(t);
            return index + 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
