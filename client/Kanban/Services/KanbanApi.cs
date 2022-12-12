using Kanban.Services.AddCard;
using Kanban.Services.AddCardList;
using Kanban.Services.CreateBoard;
using System.Net.Http.Json;
using Microsoft.JSInterop;
using System.Text.Json;
using System.Net.Http.Headers;
using Kanban.Pages.Board.Model;
using Kanban.Services.MoveCard;

namespace Kanban.Services;

public class KanbanApi
{
    private readonly HttpClient httpClient;
    private readonly IJSRuntime jsRuntime;
    private readonly StateContainerService _stateContainerService;
    private JsonSerializerOptions options = new JsonSerializerOptions
    {
        PropertyNameCaseInsensitive = true
    };

    public KanbanApi(HttpClient httpClient, IJSRuntime jsRuntime, StateContainerService stateContainerService)
    {
        this.jsRuntime = jsRuntime;
        _stateContainerService = stateContainerService;
        this.httpClient = httpClient;
    }

    public async Task<Board[]> GetAllBoards()
    {
        await SetBearerToken();
        return await httpClient.GetFromJsonAsync<Board[]>("http://dominsights.com:88/backend/board") ??
               Array.Empty<Board>();
    }

    public async Task SaveBoard(CreateBoardRequest board)
    {
        await SetBearerToken();
        await httpClient.PostAsJsonAsync("http://dominsights.com:88/backend/board", board);
    }

    public async Task<Board> GetBoard(string boardName)
    {
        await SetBearerToken();
        var board = await httpClient.GetFromJsonAsync<Board>($"http://dominsights.com:88/backend/board/{boardName}");
        if (board == null) throw new Exception("Board not returned from service.");
        _stateContainerService.SetValue(board.BoardHashCode);
        return board;
    }

    public async Task<Board> AddCardList(AddCardListRequest addCardListRequest)
    {
        await SetBearerToken();
        var responseMessage = await httpClient.PostAsJsonAsync($"http://dominsights.com:88/backend/board/{addCardListRequest.board}/cardlist", addCardListRequest);
        var json = await responseMessage.Content.ReadAsStringAsync();
        var deserialize = JsonSerializer.Deserialize<Board>(json, options)!;
        _stateContainerService.SetValue(deserialize.BoardHashCode);
        return deserialize;
    }

    public async Task<Board> AddCard(string board, AddCardRequest addCard)
    {
        await SetBearerToken();
        var responseMessage =
            await httpClient.PostAsJsonAsync($"http://dominsights.com:88/backend/board/{board}/cardlist/{addCard.cardlist}",
                addCard);
        var json = await responseMessage.Content.ReadAsStringAsync();
        var deserialize = JsonSerializer.Deserialize<Board>(json, options)!;
        _stateContainerService.SetValue(deserialize.BoardHashCode);

        return deserialize;
    }

    public async Task<Board> MoveCard(string board, string cardlist, Guid card, MoveCardRequest moveCard)
    {
        await SetBearerToken();
        var requestUri = $"http://dominsights.com:88/backend/board/{board}/cardlist/{cardlist}/cards/{card}/move";
        var responseMessage = await httpClient.PostAsJsonAsync(requestUri, moveCard);
        var json = await responseMessage.Content.ReadAsStringAsync();
        var deserialize = JsonSerializer.Deserialize<Board>(json, options)!;
        _stateContainerService.SetValue(deserialize.BoardHashCode);
        
        return deserialize;
    }

    private async Task SetBearerToken()
    {
        string json = await jsRuntime.InvokeAsync<string>("localStorage.getItem", "user");
        if (string.IsNullOrWhiteSpace(json)) return;
        JwtResponse? jwt = JsonSerializer.Deserialize<JwtResponse>(json);
        httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", jwt?.token);
    }
}
